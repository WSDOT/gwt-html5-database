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

package com.google.code.gwt.database.client.service.callback.list;

import com.google.code.gwt.database.client.SQLResultSet;
import com.google.code.gwt.database.client.SQLTransaction;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.code.gwt.database.client.service.callback.DataServiceStatementCallback;
import com.google.code.gwt.database.rebind.DataServiceGenerator;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Used in the {@link DataServiceGenerator} to reduce generated boilerplate
 * code.
 * 
 * <p>This StatementCallback impl is applied specifically to the {@link ListCallback}
 * service methods.</p>
 * 
 * @author bguijt
 */
public class StatementCallbackListCallback<T extends JavaScriptObject> extends
    DataServiceStatementCallback<T> {

  private TransactionCallbackListCallback<T> txCallback;

  /**
   * Creates a StatementCallback with a ListCallback-specific TransactionCallback
   */
  public StatementCallbackListCallback(TransactionCallbackListCallback<T> txCallback) {
    this.txCallback = txCallback;
  }
  
  @Override
  protected void storeError(int code, String message) {
    txCallback.storeStatementError(code, message);
  }

  /**
   * Stores the resultSet in the TransactionCallback
   */
  public void onSuccess(SQLTransaction transaction, SQLResultSet<T> resultSet) {
    txCallback.storeResultSet(resultSet);
  }
}
