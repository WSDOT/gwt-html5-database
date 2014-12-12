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

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Provides the result of an executed SQL Statement.
 * 
 * <p>
 * All callback methods in this interface are called in the context of a SQL
 * Transaction. This is important if you want to proceed your program after the
 * SQL statements are executed: don't do that from this callback type, use the
 * callback methods from {@link SQLTransaction} to proceed.
 * </p>
 * 
 * <p>
 * This interface specifies a type parameter to denote the type you expect to
 * return in the resultset rows. A generic row type is {@link GenericRow}, which
 * provides access to all row attributes dynamically. Use {@link GenericRow}
 * like follows:
 * </p>
 * 
 * <pre>
 * public class MyStmtCallback implements StatementCallback&lt;GenericRow&gt; {
 *     public void onSuccess(SQLTransaction tx, SQLResultSet&lt;GenericRow&gt; rs) {
 *         for (GenericRow row : rs.getRows()) {
 *             row.getString("name");  // returns the attribute 'name' from the row as a String
 *             row.getInt("age");  // returns the attribute 'age' from the row as an int
 *         }
 *     }
 *     public boolean onFailure(SQLTransaction transaction, SQLError error) {
 *         // You might want to do something here...
 *         return false;
 *     }
 * }
 * </pre>
 * 
 * @param <T> is the type which is eventually returned in the
 *          {@link SQLResultSet} at the
 *          {@link #onSuccess(SQLTransaction, SQLResultSet)} call. You can use
 *          {@link GenericRow} as a safe default type.
 * 
 * @see GenericRow
 * @see SQLTransaction
 * @see <a href="http://www.w3.org/TR/webdatabase/#sqlstatementcallback">W3C Web
 *      Database - SQLStatementCallback</a>
 * @see <a
 *      href="http://www.w3.org/TR/webdatabase/#sqlstatementerrorcallback">W3C
 *      Web Database - SQLStatementErrorCallback</a>
 * @author bguijt
 */
public interface StatementCallback<T extends JavaScriptObject> {

  /**
   * This callback method is invoked with the result of an executed SQL
   * statement (be it <code>SELECT</code>, <code>CREATE</code>,
   * <code>UPDATE</code> or anything else).
   * 
   * <p>
   * Note: <b>DO NOT</b> use this callback to proceed with your program (use
   * {@link TransactionCallback#onTransactionSuccess()} for that). <b>ONLY</b>
   * use this to process the resultSet, and/or execute additional SQL statements
   * within the same transaction!
   * </p>
   * 
   * @param transaction the transaction context of the callback, enabling
   *          additional {@link SQLTransaction#executeSql(String, Object[])}
   *          calls within the same transaction
   * @param resultSet the result of the statement
   */
  void onSuccess(SQLTransaction transaction, SQLResultSet<T> resultSet);

  /**
   * This callback method is invoked if the SQL statement fails.
   * 
   * <p>
   * Note: <b>DO NOT</b> use this callback to proceed with your program (use
   * {@link TransactionCallback#onTransactionFailure(SQLError)} for that).
   * <b>ONLY</b> use this to process the error, and/or execute additional SQL
   * statements within the same transaction!
   * </p>
   * 
   * @param transaction the transaction context of the callback, enabling
   *          additional {@link SQLTransaction#executeSql(String, Object[])}
   *          calls within the same transaction
   * @param error the SQL error causing the failure
   * @return <code>true</code> if the specified <code>transaction</code> must be
   *         rolled-back, <code>false</code> otherwise. If <code>true</code> is
   *         returned, the
   *         {@link TransactionCallback#onTransactionFailure(SQLError)} callback
   *         will be invoked directly. Otherwise, the transaction will proceed
   *         to the next {@link SQLTransaction#executeSql(String, Object[])}
   *         call, or invoke the
   *         {@link TransactionCallback#onTransactionSuccess()} callback if no
   *         SQL statements are left to execute.
   */
  boolean onFailure(SQLTransaction transaction, SQLError error);
}
