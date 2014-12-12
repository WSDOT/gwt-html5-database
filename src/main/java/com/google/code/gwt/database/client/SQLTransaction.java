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

package com.google.code.gwt.database.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;

/**
 * Represents a transaction as started by a
 * {@link Database#transaction(TransactionCallback)} or
 * {@link Database#readTransaction(TransactionCallback)} call.
 * 
 * <p>
 * You can use this class to invoke SQL statements by means of
 * {@link #executeSql(String, Object[])}. Instances of this class also mark the
 * boundaries of a transaction.
 * </p>
 * 
 * @see <a href="http://www.w3.org/TR/webdatabase/#sqltransaction">W3C Web
 *      Database - SQLTransaction</a>
 * @author bguijt
 */
public class SQLTransaction extends JavaScriptObject {

  protected SQLTransaction() {
  }

  /**
   * Helper method to bind a JS function callback to the
   * {@link StatementCallback#onSuccess(SQLTransaction, SQLResultSet)} method.
   */
  @SuppressWarnings({"unused", "unchecked"})
  private static final void handleStatement(StatementCallback callback,
      SQLTransaction transaction, SQLResultSet resultSet) {
    UncaughtExceptionHandler ueh = GWT.getUncaughtExceptionHandler();
    if (ueh != null) {
      try {
        callback.onSuccess(transaction, resultSet);
      } catch (Throwable t) {
        ueh.onUncaughtException(t);
      }
    } else {
      callback.onSuccess(transaction, resultSet);
    }
  }

  /**
   * Helper method to bind a JS function callback to the
   * {@link StatementCallback#onFailure(SQLTransaction, SQLError)} method.
   */
  @SuppressWarnings("unused")
  private static final boolean handleError(StatementCallback<?> callback,
      SQLTransaction transaction, SQLError error) {
    UncaughtExceptionHandler ueh = GWT.getUncaughtExceptionHandler();
    if (ueh != null) {
      try {
        return callback.onFailure(transaction, error);
      } catch (Throwable t) {
        ueh.onUncaughtException(t);
        return true;
      }
    }
    return callback.onFailure(transaction, error);
  }

  /**
   * Executes the provided <code>sqlStatement</code> with the specified
   * <code>arguments</code>.
   * 
   * <p>
   * The SQL is executed in a SQLite3 database. Please see the <a
   * href="http://www.sqlite.org/lang.html">SQL Language Reference</a> for its
   * capabilities.
   * </p>
   * 
   * <p>
   * Because this method doesn't specify any callback, whenever a database error
   * occurs the transaction will be rolled-back (as if a
   * {@link StatementCallback#onFailure(SQLTransaction, SQLError)} callback
   * returns <code>true</code>).
   * </p>
   * 
   * @see <a
   *      href="http://www.w3.org/TR/webdatabase/#dom-sqltransaction-executesql">W3C
   *      Web Database - SQLTransaction - executeSql</a>
   * @see <a href="http://www.sqlite.org/lang.html">SQLite3 SQL Language
   *      Reference</a>
   * @param sqlStatement the SQL statement to execute, containing
   *          <code>"?"</code> placeholders for the <code>arguments</code>
   * @param arguments the arguments to fit in the placeholders of the
   *          <code>sqlStatement</code> (could be <code>null</code>)
   */
  public final void executeSql(String sqlStatement, Object[] arguments) {
    executeSql(sqlStatement, toJsniArray(arguments));
  }

  private final native void executeSql(String sqlStatement,
      JsArrayString arguments) /*-{
    this.executeSql(sqlStatement, arguments);
  }-*/;

  /**
   * Executes the provided <code>sqlStatement</code> with the specified
   * <code>arguments</code>.
   * 
   * <p>
   * The SQL is executed in a SQLite3 database. Please see the <a
   * href="http://www.sqlite.org/lang.html">SQL Language Reference</a> for its
   * capabilities.
   * </p>
   * 
   * @see <a
   *      href="http://www.w3.org/TR/webdatabase/#dom-sqltransaction-executesql">W3C
   *      Web Database - SQLTransaction - executeSql</a>
   * @see <a href="http://www.sqlite.org/lang.html">SQLite3 SQL Language
   *      Reference</a>
   * @param sqlStatement the SQL statement to execute, containing
   *          <code>"?"</code> placeholders for the <code>arguments</code>
   * @param arguments the arguments to fit in the placeholders of the
   *          <code>sqlStatement</code> (could be <code>null</code>)
   * @param callback the callback for handling errors and the resultset of the
   *          SQL statement
   */
  public final void executeSql(String sqlStatement, Object[] arguments,
      StatementCallback<?> callback) {
    executeSql(sqlStatement, toJsniArray(arguments), callback);
  }

  @SuppressWarnings("unchecked")
  private final native void executeSql(String sqlStatement,
      JsArrayString arguments, StatementCallback callback) /*-{
    this.executeSql(
      sqlStatement,
      arguments,
      function(transaction, resultSet) {
        @com.google.code.gwt.database.client.SQLTransaction::handleStatement(Lcom/google/code/gwt/database/client/StatementCallback;Lcom/google/code/gwt/database/client/SQLTransaction;Lcom/google/code/gwt/database/client/SQLResultSet;) (callback, transaction, resultSet);
      },
      function(transaction, error) {
        return @com.google.code.gwt.database.client.SQLTransaction::handleError(Lcom/google/code/gwt/database/client/StatementCallback;Lcom/google/code/gwt/database/client/SQLTransaction;Lcom/google/code/gwt/database/client/SQLError;) (callback, transaction, error);
      }
    );
  }-*/;

  /**
   * Converts a java array to a JsArrayString.
   * 
   * <p>
   * JSNI requires a JsArray of uniform type. All array values are converted to
   * string using the toString() method before being added to the JsArrayString.
   * </p>
   * 
   * <p>
   * Note that the database takes care of converting the strings back to the
   * datatype required by the table column.
   * </p>
   * 
   * @param array to load into a JsArrayString
   * @return a JsArrayString array that can be passed to JSNI
   */
  private JsArrayString toJsniArray(Object[] array) {
    if (array == null) {
      return null;
    }
    JsArrayString jsArray = (JsArrayString) JsArrayString.createArray();
    for (int i = 0; i < array.length; i++) {
      Object value = array[i];
      jsArray.set(i, value == null ? null : value.toString());
    }
    return jsArray;
  }
}
