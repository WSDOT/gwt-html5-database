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

package com.google.code.gwt.database.client.service.callback.voyd;

import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.SQLResultSet;
import com.google.code.gwt.database.client.SQLTransaction;
import com.google.code.gwt.database.client.service.VoidCallback;
import com.google.code.gwt.database.client.service.callback.DataServiceStatementCallback;
import com.google.code.gwt.database.rebind.DataServiceGenerator;

/**
 * Used in the {@link DataServiceGenerator} to reduce generated boilerplate
 * code.
 * 
 * <p>
 * This StatementCallback impl is applied specifically to the
 * {@link VoidCallback} service methods.
 * </p>
 * 
 * @author bguijt
 */
public class StatementCallbackVoidCallback extends
    DataServiceStatementCallback<GenericRow> {

  private TransactionCallbackVoidCallback txCallback;

  /**
   * Creates a new TransactionCallback with the specified DataService' ROWIDs
   * callback.
   */
  public StatementCallbackVoidCallback(
      TransactionCallbackVoidCallback txCallback) {
    this.txCallback = txCallback;
  }

  @Override
  protected void storeError(int code, String message) {
    txCallback.storeStatementError(code, message);
  }

  /**
   * Do nothing here.
   */
  public void onSuccess(SQLTransaction transaction,
      SQLResultSet<GenericRow> resultSet) {
  }
}
