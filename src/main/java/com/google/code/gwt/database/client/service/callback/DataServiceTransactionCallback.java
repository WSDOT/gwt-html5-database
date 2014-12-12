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

package com.google.code.gwt.database.client.service.callback;

import com.google.code.gwt.database.client.SQLError;
import com.google.code.gwt.database.client.SQLTransaction;
import com.google.code.gwt.database.client.StatementCallback;
import com.google.code.gwt.database.client.TransactionCallback;
import com.google.code.gwt.database.client.service.Callback;
import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.rebind.DataServiceGenerator;

/**
 * Used in the {@link DataServiceGenerator} to reduce generated boilerplate
 * code.
 * 
 * <p>
 * The Generator implements the onTransactionStart() method body. Subclasses
 * implement the onTransactionSuccess() method body.
 * </p>
 * 
 * @param <C> the {@link Callback} type use for this transaction
 * 
 * @author bguijt
 */
public abstract class DataServiceTransactionCallback<C extends Callback>
    implements TransactionCallback {

  private C callback;

  // Context values for auditing:
  private int errCode;
  private String errMessage;
  private String sql;
  private Object[] params;

  /**
   * Creates a new TransactionCallback with the specified DataService callback.
   */
  public DataServiceTransactionCallback(C callback) {
    this.callback = callback;
  }

  /**
   * Wraps a call to {@link SQLTransaction#executeSql(String, Object[])} and
   * stores the SQL statement and parameters for failure events.
   */
  protected void exec(SQLTransaction tx, String sql, Object[] params,
      StatementCallback<?> callback) {
    this.sql = sql;
    this.params = params;
    tx.executeSql(sql, params, callback);
  }

  /**
   * Wraps a call to
   * {@link SQLTransaction#executeSql(String, Object[], StatementCallback)} and
   * stores the SQL statement and parameters for failure events.
   */
  protected void exec(SQLTransaction tx, String sql, Object[] params) {
    this.sql = sql;
    this.params = params;
    tx.executeSql(sql, params);
  }

  /**
   * Stores the {@link SQLError} details for the onFailure callback.
   * 
   * @param code the error code - see <a
   *          href="http://www.w3.org/TR/webdatabase/#sqlexception">W3C Web
   *          Database error codes</a>
   * @param message the error message
   */
  public void storeStatementError(int code, String message) {
    this.errCode = code;
    this.errMessage = message;
  }

  /**
   * Invokes the DataService' {@link Callback#onFailure(DataServiceException)}
   * callback method.
   */
  public void onTransactionFailure(SQLError error) {
    if (errMessage != null) {
      // Use the SQLError details from the
      // StatementCallback.onFailure callback:
      callback.onFailure(new DataServiceException(errMessage, errCode, sql,
          params));
    } else {
      // Use the SQLError details from the
      // TransactionCallback.onFailure callback:
      callback.onFailure(new DataServiceException(error));
    }
  }

  /**
   * Returns the DataService callback associated with this transaction.
   */
  public C getCallback() {
    return callback;
  }
}
