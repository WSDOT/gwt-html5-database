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

import com.google.code.gwt.database.client.SQLResultSet;
import com.google.code.gwt.database.client.SQLTransaction;
import com.google.code.gwt.database.client.service.ScalarCallback;
import com.google.code.gwt.database.client.service.callback.DataServiceStatementCallback;
import com.google.code.gwt.database.client.service.callback.scalar.ScalarRow;
import com.google.code.gwt.database.client.service.callback.scalar.TransactionCallbackScalarCallback;
import com.google.code.gwt.database.client.util.StringUtils;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JType;

/**
 * Represents a ServiceMethodCreator for the {@link ScalarCallback} type.
 * 
 * @author bguijt
 */
public class ServiceMethodCreatorScalarCallback extends ServiceMethodCreator {

  /**
   * Defines the types that may be associated with the ScalarCallback.
   */
  private static final Class<?>[] ALLOWED_SCALAR_TYPES = {
      Integer.class, Short.class, Byte.class, Float.class, Double.class,
      Boolean.class, String.class};

  @Override
  protected String getTransactionCallbackClassName()
      throws UnableToCompleteException {
    JType scalarType = getScalarType();
    boolean ok = false;
    for (Class<?> allowed : ALLOWED_SCALAR_TYPES) {
      if (GeneratorUtils.isType(scalarType, allowed)) {
        ok = true;
        break;
      }
    }
    if (ok) {
      return genUtils.getClassName(TransactionCallbackScalarCallback.class)
          + "<" + genUtils.getClassName(scalarType) + ">";
    }
    logger.log(TreeLogger.ERROR, "The type parameter of "
        + genUtils.getClassName(ScalarCallback.class)
        + " is unsuitable as Scalar type. Type must be one of "
        + StringUtils.join(ALLOWED_SCALAR_TYPES, ", "));
    throw new UnableToCompleteException();
  }

  @Override
  protected void generateStatementCallbackParameter()
      throws UnableToCompleteException {
    String rowType = genUtils.getClassName(ScalarRow.class) + "<"
        + genUtils.getClassName(getScalarType()) + ">";
    String rsVarName = GeneratorUtils.getVariableName("resultSet",
        service.getParameters());
    String txVarName = GeneratorUtils.getVariableName("transaction",
        service.getParameters());
    String ecVarName = GeneratorUtils.getVariableName("code",
        service.getParameters());
    String emVarName = GeneratorUtils.getVariableName("message",
        service.getParameters());

    sw.println(", new "
        + genUtils.getClassName(DataServiceStatementCallback.class) + "<"
        + rowType + ">() {");
    sw.indent();
    sw.println("public void onSuccess("
        + genUtils.getClassName(SQLTransaction.class) + " " + txVarName + ", "
        + genUtils.getClassName(SQLResultSet.class) + "<" + rowType + "> "
        + rsVarName + ") {");
    sw.indentln("storeValue(" + rsVarName + ".getRows().getItem(0).get"
        + genUtils.getClassName(getScalarType()) + "());");
    sw.println("}");

    sw.println("protected void storeError(int " + ecVarName + ", String "
        + emVarName + ") {");
    sw.indentln("storeStatementError(" + ecVarName + ", " + emVarName + ");");
    sw.println("}");

    sw.outdent();
    sw.print("}");
  }

  private JType getScalarType() {
    return callback.getType().isParameterized().getTypeArgs()[0];
  }
}
