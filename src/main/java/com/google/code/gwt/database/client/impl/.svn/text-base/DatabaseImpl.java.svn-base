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

package com.google.code.gwt.database.client.impl;

import com.google.code.gwt.database.client.Database;
import com.google.code.gwt.database.client.DatabaseException;
import com.google.code.gwt.database.client.SQLError;
import com.google.code.gwt.database.client.SQLTransaction;
import com.google.code.gwt.database.client.TransactionCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;

/**
 * Default implementation of the (native) functions for {@link Database}.
 * 
 * @author bguijt
 * @see Database
 */
public class DatabaseImpl {

  protected DatabaseImpl() {
  }

  public native boolean isSupported() /*-{
    return typeof $wnd.openDatabase != "undefined";
  }-*/;

  public Database openDatabase(String shortName, String version,
      String displayName, int maxSizeBytes) throws DatabaseException {
    try {
      return openDatabase0(shortName, version, displayName, maxSizeBytes);
    } catch (JavaScriptException e) {
      // INVALID_STATE_ERR or SECURITY_ERR
      throw new DatabaseException(e.getName(), e);
    }
  }

  private native Database openDatabase0(String shortName, String version,
      String displayName, int maxSizeBytes) /*-{
    return $wnd.openDatabase(shortName, version, displayName, maxSizeBytes);
  }-*/;

  /*
   * Helper method to bind the JS function callback to the Java
   * SQLTransactionCallback interface.
   */
  @SuppressWarnings("unused")
  private static final void handleTransaction(TransactionCallback callback,
      SQLTransaction transaction) {
    UncaughtExceptionHandler ueh = GWT.getUncaughtExceptionHandler();
    if (ueh != null) {
      try {
        callback.onTransactionStart(transaction);
      } catch (Throwable t) {
        ueh.onUncaughtException(t);
      }
    } else {
      callback.onTransactionStart(transaction);
    }
  }

  /*
   * Helper method to bind the JS function callback to the Java
   * SQLTransactionErrorCallback interface.
   */
  @SuppressWarnings("unused")
  private static final void handleError(TransactionCallback callback,
      SQLError error) {
    UncaughtExceptionHandler ueh = GWT.getUncaughtExceptionHandler();
    if (ueh != null) {
      try {
        callback.onTransactionFailure(error);
      } catch (Throwable t) {
        ueh.onUncaughtException(t);
      }
    } else {
      callback.onTransactionFailure(error);
    }
  }

  /*
   * Helper method to bind the JS function callback to the Java SQLVoidCallback
   * interface.
   */
  @SuppressWarnings("unused")
  private static final void handleSuccess(TransactionCallback callback) {
    UncaughtExceptionHandler ueh = GWT.getUncaughtExceptionHandler();
    if (ueh != null) {
      try {
        callback.onTransactionSuccess();
      } catch (Throwable t) {
        ueh.onUncaughtException(t);
      }
    } else {
      callback.onTransactionSuccess();
    }
  }

  public native void transaction(Database db, TransactionCallback callback) /*-{
    db.transaction(
      function(transaction) {
        @com.google.code.gwt.database.client.impl.DatabaseImpl::handleTransaction(Lcom/google/code/gwt/database/client/TransactionCallback;Lcom/google/code/gwt/database/client/SQLTransaction;) (callback, transaction);
      },
      function(error) {
        @com.google.code.gwt.database.client.impl.DatabaseImpl::handleError(Lcom/google/code/gwt/database/client/TransactionCallback;Lcom/google/code/gwt/database/client/SQLError;) (callback, error);
      },
      function() {
        @com.google.code.gwt.database.client.impl.DatabaseImpl::handleSuccess(Lcom/google/code/gwt/database/client/TransactionCallback;) (callback);
      }
    );
  }-*/;

  public native void readTransaction(Database db, TransactionCallback callback) /*-{
    db.readTransaction(
      function(transaction) {
        @com.google.code.gwt.database.client.impl.DatabaseImpl::handleTransaction(Lcom/google/code/gwt/database/client/TransactionCallback;Lcom/google/code/gwt/database/client/SQLTransaction;) (callback, transaction);
      },
      function(error) {
        @com.google.code.gwt.database.client.impl.DatabaseImpl::handleError(Lcom/google/code/gwt/database/client/TransactionCallback;Lcom/google/code/gwt/database/client/SQLError;) (callback, error);
      },
      function() {
        @com.google.code.gwt.database.client.impl.DatabaseImpl::handleSuccess(Lcom/google/code/gwt/database/client/TransactionCallback;) (callback);
      }
    );
  }-*/;

  public native void changeVersion(Database db, String oldVersion,
      String newVersion, TransactionCallback callback) /*-{
    db.changeVersion(
      oldVersion,
      newVersion,
      function(transaction) {
        @com.google.code.gwt.database.client.impl.DatabaseImpl::handleTransaction(Lcom/google/code/gwt/database/client/TransactionCallback;Lcom/google/code/gwt/database/client/SQLTransaction;) (callback, transaction);
      },
      function(error) {
        @com.google.code.gwt.database.client.impl.DatabaseImpl::handleError(Lcom/google/code/gwt/database/client/TransactionCallback;Lcom/google/code/gwt/database/client/SQLError;) (callback, error);
      },
      function() {
        @com.google.code.gwt.database.client.impl.DatabaseImpl::handleSuccess(Lcom/google/code/gwt/database/client/TransactionCallback;) (callback);
      }
    );
  }-*/;

  public native String getVersion(Database db) /*-{
    return db.version;
  }-*/;
}
