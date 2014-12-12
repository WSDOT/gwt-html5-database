/*
 * Copyright 2009 Bart Guijt and others.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.code.gwt.database.rebind;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;

import com.google.code.gwt.database.client.Database;
import com.google.code.gwt.database.client.DatabaseException;
import com.google.code.gwt.database.client.SQLTransaction;
import com.google.code.gwt.database.client.service.Callback;
import com.google.code.gwt.database.client.service.Connection;
import com.google.code.gwt.database.client.service.DataService;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.code.gwt.database.client.service.RowIdListCallback;
import com.google.code.gwt.database.client.service.ScalarCallback;
import com.google.code.gwt.database.client.service.Select;
import com.google.code.gwt.database.client.service.Update;
import com.google.code.gwt.database.client.service.VoidCallback;
import com.google.code.gwt.database.client.service.callback.DataServiceStatementCallback;
import com.google.code.gwt.database.client.service.callback.list.StatementCallbackListCallback;
import com.google.code.gwt.database.client.service.callback.list.TransactionCallbackListCallback;
import com.google.code.gwt.database.client.service.callback.rowid.StatementCallbackRowIdListCallback;
import com.google.code.gwt.database.client.service.callback.rowid.TransactionCallbackRowIdListCallback;
import com.google.code.gwt.database.client.service.callback.scalar.TransactionCallbackScalarCallback;
import com.google.code.gwt.database.client.service.callback.voyd.StatementCallbackVoidCallback;
import com.google.code.gwt.database.client.service.callback.voyd.TransactionCallbackVoidCallback;
import com.google.code.gwt.database.client.service.impl.BaseDataService;
import com.google.code.gwt.database.client.service.impl.DataServiceUtils;
import com.google.code.gwt.database.client.util.StringUtils;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JPackage;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.dev.util.Util;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Helper class for the {@link DataServiceGenerator}.
 * 
 * <p>
 * This class is specifically instantiated for a single DataService proxy.
 * </p>
 * 
 * @author bguijt
 */
public class SqlProxyCreator {

  private static final String PROXY_SUFFIX = "_SqlProxy";

  private static final String[] IMPORTED_CLASSES = new String[] {
      Database.class.getCanonicalName(),
      SQLTransaction.class.getCanonicalName(), BaseDataService.class.getName(),
      DataServiceUtils.class.getCanonicalName(),
      VoidCallback.class.getCanonicalName(),
      ListCallback.class.getCanonicalName(),
      ScalarCallback.class.getCanonicalName(),
      RowIdListCallback.class.getCanonicalName(),
      DataServiceStatementCallback.class.getCanonicalName(),
      StatementCallbackVoidCallback.class.getCanonicalName(),
      StatementCallbackListCallback.class.getCanonicalName(),
      StatementCallbackRowIdListCallback.class.getCanonicalName(),
      TransactionCallbackVoidCallback.class.getCanonicalName(),
      TransactionCallbackScalarCallback.class.getCanonicalName(),
      TransactionCallbackListCallback.class.getCanonicalName(),
      TransactionCallbackRowIdListCallback.class.getCanonicalName(),
      DatabaseException.class.getCanonicalName()};

  private TreeLogger logger;
  private GeneratorContext context;
  private GeneratorUtils genUtils;
  private JClassType dataService;
  private SourceWriter sw;

  /**
   * <code>true</code> if the dataService directly extends DataService,
   * <code>false</code> otherwise.
   */
  private boolean isBaseType;

  public SqlProxyCreator(TreeLogger logger, GeneratorContext context,
      JClassType dataService) {
    this.logger = logger;
    this.context = context;
    this.dataService = dataService;
    this.genUtils = new GeneratorUtils(logger, context, IMPORTED_CLASSES);
  }

  public String create() throws UnableToCompleteException {
    sw = getSourceWriter();
    if (sw == null) {
      // No need to generate, it's already done. Return name of generated class.
      return getProxyQualifiedName();
    }

    generateProxyConstructor();
    if (isBaseType) {
      generateProxyOpenDatabaseMethod();
      generateProxyGetDatabaseDetailsMethod();
    }

    // Generate service methods for each defined interface method:
    for (JMethod method : dataService.getMethods()) {
      generateProxyServiceMethod(method);
    }

    sw.commit(logger);

    return getProxyQualifiedName();
  }

  /**
   * Generates the constructor.
   */
  private void generateProxyConstructor() {
    sw.println("public " + getProxySimpleName() + "() {");
    sw.indent();
    sw.println("// default empty constructor");
    sw.outdent();
    sw.println("}");
  }

  /**
   * Generates the {@link BaseDataService#openDatabase()} method
   */
  private void generateProxyOpenDatabaseMethod() {
    Connection con = dataService.getAnnotation(Connection.class);
    sw.beginJavaDocComment();
    sw.print("Opens the '" + con.name() + "' Database version " + con.version());
    sw.endJavaDocComment();
    sw.println("public final " + genUtils.getClassName(Database.class)
        + " openDatabase() throws "
        + genUtils.getClassName(DatabaseException.class) + " {");
    sw.indentln("return " + genUtils.getClassName(Database.class)
        + ".openDatabase(\"" + Generator.escape(con.name()) + "\", \""
        + Generator.escape(con.version()) + "\", \""
        + Generator.escape(con.description()) + "\", " + con.maxsize() + ");");
    sw.println("}");
  }

  /**
   * Generates the {@link BaseDataService#getDatabaseDetails()} method.
   */
  private void generateProxyGetDatabaseDetailsMethod() {
    Connection con = dataService.getAnnotation(Connection.class);
    String toReturn = "'" + Generator.escape(con.name()) + "' version "
        + Generator.escape(con.version());
    sw.beginJavaDocComment();
    sw.print("Returns the <code>" + toReturn + "</code> string.");
    sw.endJavaDocComment();
    sw.println("public final String getDatabaseDetails() {");
    sw.indentln("return \"" + toReturn + "\";");
    sw.println("}");
  }

  /**
   * Generates the proxy method implementing the specified service.
   */
  private void generateProxyServiceMethod(JMethod service)
      throws UnableToCompleteException {
    Select select = service.getAnnotation(Select.class);
    Update update = service.getAnnotation(Update.class);

    // Assertions:
    if (select == null && update == null) {
      logger.log(TreeLogger.ERROR, service.getName()
          + " has no @Select nor @Update annotation");
      throw new UnableToCompleteException();
    }
    if ((select == null || StringUtils.isEmpty(getSql(select)))
        && (update == null || StringUtils.isEmpty(getSql(update)))) {
      logger.log(TreeLogger.ERROR, service.getName()
          + ": @Select or @Update annotation has no SQL statement");
      throw new UnableToCompleteException();
    }
    JParameter[] params = service.getParameters();
    if (params.length == 0) {
      logger.log(TreeLogger.ERROR, "Method " + service.getName()
          + " must have at least one (callback) parameter");
      throw new UnableToCompleteException();
    }
    JParameter callback = params[params.length - 1];
    if (!genUtils.isAssignableToType(callback.getType(), Callback.class)) {
      logger.log(TreeLogger.ERROR, "The last parameter of method "
          + service.getName() + " is no valid Callback! Must be subtype of "
          + genUtils.getClassName(Callback.class));
      throw new UnableToCompleteException();
    }

    generateProxyServiceMethodJavadoc(service);

    sw.print("public final void " + service.getName() + "(");
    for (int i = 0; i < params.length; i++) {
      if (i > 0) {
        sw.print(", ");
      }
      sw.print("final " + genUtils.getClassName(params[i].getType()) + " "
          + params[i].getName());
    }
    sw.println(") {");
    sw.indent();

    // Depending on the callback type, create a service method body:
    ServiceMethodCreator creator = update != null ? createExecuteSqlCreator(
        service, getSql(update), update.foreach(), update)
        : createExecuteSqlCreator(service, getSql(select), null, select);

    creator.generateServiceMethodBody();

    // ends service method
    sw.outdent();
    sw.println("}");
  }

  /**
   * Generates the Javadoc for the specified service method. The usefulness of
   * this code is arguable low :-)
   */
  private void generateProxyServiceMethodJavadoc(JMethod service)
      throws UnableToCompleteException {
    Select select = service.getAnnotation(Select.class);
    Update update = service.getAnnotation(Update.class);
    sw.beginJavaDocComment();
    String stmt = null;
    sw.println("Executes the following SQL "
        + (update != null ? "Update" : "Select") + " statement:");
    stmt = update != null ? getSql(update) : getSql(select);
    sw.print("<pre>" + Util.escapeXml(stmt) + "</pre>");
    sw.endJavaDocComment();
  }

  private String getSql(Select select) {
    return (StringUtils.isEmpty(select.value())) ? select.sql()
        : select.value();
  }

  private String getSql(Update update) {
    return (StringUtils.isEmpty(update.value())) ? update.sql()
        : update.value();
  }

  private ServiceMethodCreator createExecuteSqlCreator(JMethod service,
      String sql, String foreach, Annotation query)
      throws UnableToCompleteException {
    JParameter callback = service.getParameters()[service.getParameters().length - 1];
    String serviceCreatorName = ServiceMethodCreator.class.getCanonicalName()
        + callback.getType().getSimpleSourceName();
    TreeLogger branchedLogger = logger.branch(TreeLogger.DEBUG,
        "Generating service method '" + service.getName()
            + "' with SQL statement expression '" + sql + "'");
    try {
      Class<?> creatorClass = Class.forName(serviceCreatorName);
      ServiceMethodCreator creator = (ServiceMethodCreator) creatorClass.newInstance();
      creator.setContext(context, branchedLogger, sw, service, sql, foreach,
          query, genUtils.branchWithLogger(branchedLogger));
      return creator;
    } catch (ClassNotFoundException e) {
      branchedLogger.log(TreeLogger.ERROR, "Cannot find " + serviceCreatorName
          + " class specific for callback type '"
          + callback.getType().getSimpleSourceName() + "'");
    } catch (InstantiationException e) {
      branchedLogger.log(TreeLogger.ERROR, "Cannot instantiate "
          + serviceCreatorName + " class specific for callback type '"
          + callback.getType().getSimpleSourceName() + "'");
    } catch (IllegalAccessException e) {
      branchedLogger.log(TreeLogger.ERROR, "Cannot invoke constructor of "
          + serviceCreatorName + " class specific for callback type '"
          + callback.getType().getSimpleSourceName() + "'");
    }
    throw new UnableToCompleteException();
  }

  /**
   * Returns a SourceWriter which is prepared to write the class' body.
   * 
   * @throws UnableToCompleteException
   */
  private SourceWriter getSourceWriter() throws UnableToCompleteException {
    JPackage serviceIntfPkg = dataService.getPackage();
    String packageName = serviceIntfPkg == null ? "" : serviceIntfPkg.getName();
    PrintWriter printWriter = context.tryCreate(logger, packageName,
        getProxySimpleName());

    if (printWriter == null) {
      // Proxy already exists.
      return null;
    }

    ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(
        packageName, getProxySimpleName());

    for (String imp : IMPORTED_CLASSES) {
      composerFactory.addImport(imp);
    }

    JClassType superType = dataService.getImplementedInterfaces()[0];
    if (GeneratorUtils.isType(superType, DataService.class)) {
      composerFactory.setSuperclass(genUtils.getClassName(BaseDataService.class));
      isBaseType = true;
    } else {
      isBaseType = false;
      // the dataService inherits from a different interface.
      // Create another SqlProxyCreator to take care of that interface:
      SqlProxyCreator superClassCreator = new SqlProxyCreator(logger.branch(
          TreeLogger.DEBUG, "Generating proxy methods for superclass '"
              + superType.getQualifiedSourceName() + "'..."), context,
          superType);
      String className = superClassCreator.create();
      composerFactory.setSuperclass(genUtils.shortenName(className));
    }
    composerFactory.addImplementedInterface(genUtils.getClassName(dataService));

    composerFactory.setJavaDocCommentForClass("Generated by {@link "
        + genUtils.getClassName(getClass()) + "}");

    return composerFactory.createSourceWriter(context, printWriter);
  }

  /**
   * Returns the fully qualified name of the generated class.
   */
  private String getProxyQualifiedName() {
    return (dataService.getPackage() == null ? ""
        : dataService.getPackage().getName() + ".")
        + getProxySimpleName();
  }

  /**
   * Returns the name of the generated class.
   */
  private String getProxySimpleName() {
    return dataService.getName().replace('.', '_') + PROXY_SUFFIX;
  }
}
