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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.google.code.gwt.database.client.SQLTransaction;
import com.google.code.gwt.database.client.TransactionCallback;
import com.google.code.gwt.database.client.service.Callback;
import com.google.code.gwt.database.client.service.Update;
import com.google.code.gwt.database.client.service.impl.DataServiceUtils;
import com.google.code.gwt.database.client.util.StringUtils;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Base class representing a tx.executeSql(..) call creator for each type of
 * {@link Callback}.
 * 
 * @author bguijt
 */
public abstract class ServiceMethodCreator {

  protected GeneratorContext context;
  protected TreeLogger logger;
  protected SourceWriter sw;
  protected JMethod service;
  protected String sql;
  protected String foreach;
  protected Annotation query;
  protected GeneratorUtils genUtils;

  protected JParameter callback;
  protected String txVarName;

  /**
   * Sets the context for generating the Transaction Callback.
   */
  public void setContext(GeneratorContext context, TreeLogger logger,
      SourceWriter sw, JMethod service, String sql, String foreach,
      Annotation query, GeneratorUtils genUtils) {
    this.context = context;
    this.logger = logger;
    this.sw = sw;
    this.service = service;
    this.sql = sql;
    this.foreach = foreach;
    this.query = query;
    this.genUtils = genUtils;
    this.callback = service.getParameters()[service.getParameters().length - 1];
    this.txVarName = GeneratorUtils.getVariableName("tx",
        service.getParameters());
  }

  /**
   * Generates the actual service method body.
   */
  public void generateServiceMethodBody() throws UnableToCompleteException {
    String txMethodName = query.annotationType().equals(Update.class)
        ? "transaction" : "readTransaction";
    sw.println(txMethodName + "(new " + getTransactionCallbackClassName() + "("
        + callback.getName() + ") {");
    sw.indent();

    generateTransactionCallbackBody();

    // ends new TransactionCallback() and (read)transaction() call
    sw.outdent();
    sw.println("});");
  }

  /**
   * Generates the body of the {@link TransactionCallback} type.
   * 
   * <p>
   * By default it only generates the onTransactionStart() method, which in turn
   * calls the {@link #generateOnTransactionStartBody()} method.
   * </p>
   * 
   * @throws UnableToCompleteException
   */
  protected void generateTransactionCallbackBody()
      throws UnableToCompleteException {
    sw.println("public void onTransactionStart("
        + genUtils.getClassName(SQLTransaction.class) + " " + txVarName + ") {");
    sw.indent();

    generateOnTransactionStartBody();

    // ends onTransactionStart()
    sw.outdent();
    sw.println("}");
  }

  /**
   * Generates the body of an onTransactionStart() method.
   */
  protected void generateOnTransactionStartBody()
      throws UnableToCompleteException {
    generateExecuteSqlStatement();
  }

  /**
   * Returns the name of the TransactionCallback implementation use for this
   * Service Method creator.
   */
  protected abstract String getTransactionCallbackClassName()
      throws UnableToCompleteException;

  /**
   * Generates an iterating <code>tx.executeSql(...);</code> call statement.
   * 
   * @throws UnableToCompleteException
   */
  protected void generateExecuteIteratedSqlStatements()
      throws UnableToCompleteException {
    if (StringUtils.isNotEmpty(foreach)) {
      // Generate code to loop over a collection to create a tx.executeSql()
      // call for each item.

      // Ensure no parameters are specified on the service method named '_':
      if (!"_".equals(GeneratorUtils.getVariableName("_",
          service.getParameters()))) {
        logger.log(TreeLogger.ERROR,
            "The service method cannot apply a parameter named '_' when "
                + "the 'foreach' attribute is also specified on the @Update "
                + "annotation");
        throw new UnableToCompleteException();
      }

      // Find the types, parameters, assert not-nulls, etc.:
      JType collection = GeneratorUtils.findType(foreach,
          service.getParameters());
      String forEachType = null;
      if (collection == null) {
        logger.log(TreeLogger.WARN,
            "no parameter on the service method named '" + foreach
                + "' found. Using Object as the type for the loop variable '_'");
      } else if (collection.isParameterized() != null) {
        forEachType = genUtils.getTypeParameter(collection);
      } else if (collection.isArray() != null) {
        forEachType = genUtils.getClassName(collection.isArray().getComponentType());
      }
      if (forEachType == null) {
        forEachType = "Object";
      }

      sw.println("for (" + forEachType + " _ : " + foreach + ") {");
      sw.indent();
      generateExecuteSqlStatement();
      sw.outdent();
      sw.println("}");
    }
  }

  /**
   * Generates a <code>tx.executeSql(...);</code> call statement.
   * 
   * @throws UnableToCompleteException
   */
  protected void generateExecuteSqlStatement() throws UnableToCompleteException {
    List<String> tokenizedStmt = tokenizeSql(sql);
    if (tokenizedStmt.size() == 0) {
      // No SQL at all. Probably already captured earlier in the process.
      logger.log(TreeLogger.ERROR, "No SQL statement specified");
      throw new UnableToCompleteException();
    }

    if (tokenizedStmt.size() == 1) {
      // No parameters used in the SQL:
      sw.print("exec(" + txVarName + ", "
          + StringUtils.getEscapedString(tokenizedStmt.get(0)) + ", null");
    } else {
      // At least one parameter used in the SQL:
      String paramsVarName = GeneratorUtils.getVariableName("params",
          service.getParameters());

      StringBuilder prepParamsArrayStatic = new StringBuilder("Object[] ").append(
          paramsVarName).append(" = {");
      StringBuilder prepParamsArrayDynamic = new StringBuilder("Object[] ").append(
          paramsVarName).append(" = new Object[");
      // Determine amount of parameters (to size the array) and whether dynamic
      // parameters are applied:
      boolean hasDynamics = false;
      for (int i = 0; i < tokenizedStmt.size(); i++) {
        if ((i % 2) == 0) {
          // SQL token:
          // ignore while determining parameters
        } else {
          // Parameter token:
          String expression = tokenizedStmt.get(i);
          if (i > 1) {
            prepParamsArrayDynamic.append(" + ");
            prepParamsArrayStatic.append(", ");
          }
          if (isDynamicParameter(expression)) {
            // Aha! We have a collection or array used as input parameter.
            // This means some different statement builder code!
            hasDynamics = true;
            JType type = GeneratorUtils.findType(expression,
                service.getParameters());
            if (type.isArray() != null) {
              prepParamsArrayDynamic.append(expression + ".length");
            } else {
              prepParamsArrayDynamic.append(genUtils.getClassName(DataServiceUtils.class)
                  + ".getSize(" + expression + ")");
            }
          } else {
            prepParamsArrayDynamic.append("1");
            prepParamsArrayStatic.append(expression);
          }
        }
      }

      // Now we determined whether the SQL statement to generate incorporates
      // dynamic list(s) of parameters (e.g. IN() statements).
      // This greatly influences the Java code to generate:
      String sqlVarName = GeneratorUtils.getVariableName("sql",
          service.getParameters());
      String indexVarName = GeneratorUtils.getVariableName("i",
          service.getParameters());
      if (hasDynamics) {
        prepParamsArrayDynamic.append("];");
        sw.println(prepParamsArrayDynamic.toString());
        sw.println("int " + indexVarName + " = 0;");
        sw.println("StringBuilder " + sqlVarName + " = new StringBuilder();");
      } else {
        prepParamsArrayStatic.append("};");
        sw.println(prepParamsArrayStatic.toString());
      }

      // Define statement in sqlVarName, and parameters in paramsVarName:
      StringBuilder sqlLiteral = new StringBuilder();
      for (int i = 0; i < tokenizedStmt.size(); i++) {
        String token = tokenizedStmt.get(i);
        if ((i % 2) == 0) {
          // SQL token:
          sqlLiteral.append(token);
        } else {
          // Parameter token:
          if (isDynamicParameter(token)) {
            sw.println(sqlVarName + ".append("
                + StringUtils.getEscapedString(sqlLiteral.toString()) + ");");
            sqlLiteral = new StringBuilder();

            sw.println(indexVarName + " = "
                + genUtils.getClassName(DataServiceUtils.class)
                + ".addParameter(" + sqlVarName + ", " + paramsVarName + ", "
                + indexVarName + ", " + token + ");");
          } else {
            sqlLiteral.append("?");
            if (hasDynamics) {
              sw.println(paramsVarName + "[" + indexVarName + "++] = " + token
                  + ";");
            }
          }
        }
      }

      if (hasDynamics) {
        if (sqlLiteral.length() > 0) {
          sw.println(sqlVarName + ".append("
              + StringUtils.getEscapedString(sqlLiteral.toString()) + ");");
        }

        // Invoke the actual executeSql method:
        sw.print("exec(" + txVarName + ", " + sqlVarName + ".toString(), "
            + paramsVarName);
      } else {
        // Invoke the actual executeSql method with a String literal:
        sw.print("exec(" + txVarName + ", "
            + StringUtils.getEscapedString(sqlLiteral.toString()) + ", "
            + paramsVarName);
      }
    }

    generateStatementCallbackParameter();

    sw.println(");");
  }

  /**
   * generates the callback parameter expression (to <code>sw</code>).
   * 
   * <p>
   * It is this part in bold:
   * </p>
   * 
   * <pre>tx.executeSQL("SELECT....", parameters<b>, new MyCallback()</b>);</pre>
   * <p>
   * Please mind that you can either output nothing, or comma + expression.
   * </p>
   * 
   * @throws UnableToCompleteException
   */
  protected abstract void generateStatementCallbackParameter()
      throws UnableToCompleteException;

  /**
   * Returns the specified SQL expression in at least one part.
   * 
   * <p>
   * The first part represents an SQL expression where each {} parameter is
   * substituted for a '?' character; the other parts represent the substituted
   * expressions.
   * </p>
   * 
   * <p>
   * This means that the following stmt:
   * </p>
   * 
   * <pre>INSERT INTO clickcount (clicked) VALUES ({when.getTime()})</pre>
   * <p>
   * will be tokenized to the following List:
   * </p>
   * <ul>
   * <li><code>INSERT INTO clickcount (clicked) VALUES (</code></li>
   * <li><code>when.getTime()</code></li>
   * <li><code>)</code></li>
   * </ul>
   * 
   * <p>
   * The list will *always* start with a SQL literal token, followed by a
   * parameter token, next another SQL literal token, etc. Never two tokens of
   * the same kind after one another!
   * </p>
   */
  private List<String> tokenizeSql(String stmt)
      throws UnableToCompleteException {
    List<String> result = new ArrayList<String>();
    StringBuilder token = new StringBuilder();
    int depth = 0;
    for (int i = 0; i < stmt.length(); i++) {
      char ch = stmt.charAt(i);
      switch (ch) {
        case '{':
          if (depth == 0) {
            // End previous token:
            if (token.length() == 0) {
              logger.log(TreeLogger.ERROR,
                  "Cannot start SQL statement with a '{...}' parameter, "
                      + "nor can one follow immediately after another "
                      + "(e.g. '{...}{...}')");
              throw new UnableToCompleteException();
            }
            result.add(token.toString());
            // Start a parameter:
            token = new StringBuilder();
          } else {
            token.append(ch);
          }
          depth++;
          break;
        case '}':
          depth--;
          if (depth == 0) {
            // End a parameter:
            String s = token.toString().trim();
            if (s.length() == 0) {
              logger.log(TreeLogger.ERROR,
                  "Parameter expression in SQL statement is empty!");
              throw new UnableToCompleteException();
            }
            result.add(s);
            token = new StringBuilder();
          } else if (depth < 0) {
            logger.log(TreeLogger.ERROR,
                "Parameter expression in SQL statement is not closed"
                    + " correctly! Too many closing brace(s)");
            throw new UnableToCompleteException();
          } else {
            token.append(ch);
          }
          break;
        default:
          token.append(ch);
          break;
      }
    }
    if (depth > 0) {
      logger.log(TreeLogger.ERROR, "Parameter expression(s) in SQL statement"
          + " is not closed correctly! Missing " + depth + " closing brace(s)");
      throw new UnableToCompleteException();
    }
    result.add(token.toString());
    return result;
  }

  /**
   * Appends a parameter to the sql String.
   * 
   * <p>
   * Returns <code>true</code> if the specified <code>expression</code> is an
   * <code>{@link Iterable}&lt;? extends {@link Number}&gt;</code>,
   * <code>{@link Iterable}&lt;? extends {@link String}&gt;</code>.
   * <code>{@link String}[]</code>, <code>{@link Number}[]</code>, <code>{@link primitive}[]</code>.
   * </p>
   */
  private boolean isDynamicParameter(String expression)
      throws UnableToCompleteException {
    JType type = GeneratorUtils.findType(expression, service.getParameters());
    boolean isSuitableDynamic = false;
    String typeParam = null;
    if (type != null) {
      if (genUtils.isAssignableToType(type, Iterable.class)) {
        // OK, we've got our collection. Is the Type parameter 'suitable'?
        typeParam = genUtils.getTypeParameter(type);
        for (String t : new String[] {
            "String", "Integer", "Number", "Long", "Short", "Double", "Float",
            "Boolean"}) {
          if (typeParam.equals(t)) {
            isSuitableDynamic = true;
            break;
          }
        }
        if (!isSuitableDynamic) {
          logger.log(TreeLogger.ERROR, "The expression in the SQL statement '"
              + expression + "' is defined as an Iterable, but its type "
              + "parameter " + typeParam
              + " is NOT one of String, Long, Integer, Short, Number, "
              + "Double, Float, Boolean");
          throw new UnableToCompleteException();
        }
      }
      if (type.isArray() != null) {
        // Same as Collection, really:
        typeParam = type.isArray().getComponentType().getSimpleSourceName();
        for (String t : new String[] {
            "String", "Integer", "int", "Number", "Long", "Short", "Double",
            "Float", "Boolean"}) {
          if (typeParam.equalsIgnoreCase(t)) {
            isSuitableDynamic = true;
            break;
          }
        }
        if (!isSuitableDynamic) {
          logger.log(TreeLogger.ERROR, "The expression in the SQL statement '"
              + expression
              + "' is defined as an Array, but its component type " + typeParam
              + " is NOT one of String, Long/long, Integer/int, "
              + "Short/short, Number, Double/double, Float/float, "
              + "Boolean/boolean");
          throw new UnableToCompleteException();
        }
      }
    }
    return isSuitableDynamic;
  }
}
