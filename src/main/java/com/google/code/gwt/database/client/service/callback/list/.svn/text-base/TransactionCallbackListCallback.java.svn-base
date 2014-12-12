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
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.code.gwt.database.client.service.callback.DataServiceTransactionCallback;
import com.google.code.gwt.database.rebind.DataServiceGenerator;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Used in the {@link DataServiceGenerator} to reduce generated boilerplate
 * code.
 * 
 * <p>
 * The Generator implements the onTransactionStart() method body.
 * </p>
 * 
 * @author bguijt
 */
public abstract class TransactionCallbackListCallback<T extends JavaScriptObject>
    extends DataServiceTransactionCallback<ListCallback<T>> {

  private ResultSetList<T> store;

  /**
   * Creates a new TransactionCallback with the specified DataService' List
   * callback.
   */
  public TransactionCallbackListCallback(ListCallback<T> callback) {
    super(callback);
  }

  /**
   * Store the resultSet for later retrieval when the transaction has ended.
   */
  protected void storeResultSet(SQLResultSet<T> resultSet) {
    store = new ResultSetList<T>(resultSet);
  }

  /**
   * Invokes the DataService' {@link ListCallback#onSuccess(java.util.List)}
   * callback method with the value stored at
   * {@link #storeResultSet(SQLResultSet)}.
   */
  public void onTransactionSuccess() {
    getCallback().onSuccess(store);
  }
}
