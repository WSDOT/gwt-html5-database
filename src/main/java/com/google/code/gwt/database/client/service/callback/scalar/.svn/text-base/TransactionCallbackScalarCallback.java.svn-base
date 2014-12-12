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

package com.google.code.gwt.database.client.service.callback.scalar;

import com.google.code.gwt.database.client.service.ScalarCallback;
import com.google.code.gwt.database.client.service.callback.DataServiceTransactionCallback;
import com.google.code.gwt.database.rebind.DataServiceGenerator;

/**
 * Used in the {@link DataServiceGenerator} to reduce generated boilerplate
 * code.
 * 
 * <p>
 * The Generator implements the onTransactionStart() method body.
 * </p>
 * 
 * @param <T> represents the type for the scalar value
 * 
 * @author bguijt
 */
public abstract class TransactionCallbackScalarCallback<T> extends
    DataServiceTransactionCallback<ScalarCallback<T>> {

  private T store = null;

  /**
   * Creates a new TransactionCallback with the specified DataService' Scalar
   * callback.
   */
  public TransactionCallbackScalarCallback(ScalarCallback<T> callback) {
    super(callback);
  }

  /**
   * Invokes the DataService' {@link ScalarCallback#onSuccess(Object)} callback
   * method with the value stored at {@link #storeValue(Object)}.
   */
  public void onTransactionSuccess() {
    getCallback().onSuccess(store);
  }

  /**
   * Store the value for later retrieval when the transaction has ended.
   */
  protected void storeValue(T value) {
    store = value;
  }
}
