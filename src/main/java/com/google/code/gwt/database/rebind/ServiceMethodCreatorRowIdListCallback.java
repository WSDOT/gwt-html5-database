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

import com.google.code.gwt.database.client.service.RowIdListCallback;
import com.google.code.gwt.database.client.service.callback.rowid.StatementCallbackRowIdListCallback;
import com.google.code.gwt.database.client.service.callback.rowid.TransactionCallbackRowIdListCallback;
import com.google.code.gwt.database.client.util.StringUtils;
import com.google.gwt.core.ext.UnableToCompleteException;

/**
 * Represents a ServiceMethodCreator for the {@link RowIdListCallback} type.
 * 
 * @author bguijt
 */
public class ServiceMethodCreatorRowIdListCallback extends ServiceMethodCreator {

  @Override
  protected String getTransactionCallbackClassName()
      throws UnableToCompleteException {
    return genUtils.getClassName(TransactionCallbackRowIdListCallback.class);
  }

  @Override
  public void generateOnTransactionStartBody() throws UnableToCompleteException {
    // Use a single instance for each tx.executeSql()
    // call in the iteration:
    String stmtCallbackName = genUtils.getClassName(StatementCallbackRowIdListCallback.class);

    // Holds the name of a pre-instantiated StatementCallback type:
    String callbackInstanceName = GeneratorUtils.getVariableName(
        "rowIdListCallback", service.getParameters());

    sw.println("final " + stmtCallbackName + " " + callbackInstanceName
        + " = new " + stmtCallbackName + "(this);");

    if (StringUtils.isNotEmpty(foreach)) {
      generateExecuteIteratedSqlStatements();
    } else {
      generateExecuteSqlStatement();
    }
  }
  
  @Override
  protected void generateStatementCallbackParameter()
      throws UnableToCompleteException {
    // Holds the name of a pre-instantiated StatementCallback type:
    String callbackInstanceName = GeneratorUtils.getVariableName(
        "rowIdListCallback", service.getParameters());

    sw.print(", " + callbackInstanceName);
  }
}
