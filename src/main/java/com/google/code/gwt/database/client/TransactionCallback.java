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

/**
 * Represents callbacks which are invoked when a database transaction is in
 * progress.
 * 
 * <p>
 * First, the {@link #onTransactionStart(SQLTransaction)} is invoked. In this
 * method you should execute your SQL statements by means of
 * {@link SQLTransaction#executeSql(String, Object[])} calls.
 * </p>
 * <p>
 * If an error occurs during the transaction (which means: some
 * {@link StatementCallback#onFailure(SQLTransaction, SQLError)} callback
 * returned <code>true</code>), {@link #onTransactionFailure(SQLError)} is
 * invoked. Otherwise (if the transaction completes successfully),
 * {@link #onTransactionSuccess()} is invoked.
 * </p>
 * 
 * @see <a href="http://www.w3.org/TR/webdatabase/#transaction-steps">W3C Web
 *      Database - Transaction steps</a>
 * @author bguijt
 */
public interface TransactionCallback {

  /**
   * The implementation of this method should execute the actual SQL transaction
   * by using the specified <code>transaction</code> instance to invoke
   * {@link SQLTransaction#executeSql(String, Object[])}.
   * 
   * @see <a href="http://www.w3.org/TR/webdatabase/#sqltransactioncallback">W3C
   *      Web Database - SQLTransactionCallback</a>
   * @param transaction the transaction context
   */
  void onTransactionStart(SQLTransaction transaction);

  /**
   * This callback is invoked if all SQL statements in the transaction are
   * committed successfully.
   * 
   * <p>
   * The transaction is closed now. Use this method to proceed with your
   * program.
   * </p>
   * 
   * @see <a href="http://www.w3.org/TR/webdatabase/#sqlvoidcallback">W3C Web
   *      Database - SQLVoidCallback</a>
   */
  void onTransactionSuccess();

  /**
   * This callback is invoked when the transaction has failed to complete
   * successfully.
   * 
   * <p>
   * The transaction is closed now. Use this method to proceed with your
   * program.
   * </p>
   * 
   * @see <a
   *      href="http://www.w3.org/TR/webdatabase/#sqltransactionerrorcallback">W3C
   *      Web Database - SQLTransactionErrorCallback</a>
   */
  void onTransactionFailure(SQLError error);
}
