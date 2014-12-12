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

import com.google.code.gwt.database.client.impl.DatabaseImpl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Implements the Asynchronous Database interface, modeled after the <a
 * href="http://www.w3.org/TR/webdatabase/#sql">W3C Web Database</a>.
 * 
 * <h2>Usage</h2>
 * <p>
 * Start by obtaining a handle to a <code>Database</code> instance by calling
 * {@link #openDatabase(String, String, String, int)}:
 * </p>
 * 
 * <pre>
 * Database db = Database.openDatabase("testdb", "1.0", "Test DB", 10000);
 * </pre>
 * 
 * <p>
 * From there, start a transaction:
 * </p>
 * 
 * <pre>
 * db.transaction(new TransactionCallback() {
 *     public void onTransactionStart(SQLTransaction tx) {
 *         tx.executeSql(
 *             "CREATE TABLE IF NOT EXISTS contacts (" +
 *             "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
 *             "name VARCHAR(30) NOT NULL, " +
 *             "email VARCHAR(100) NOT NULL, " +
 *             "country VARCHAR(2) NOT NULL);", null);
 *     }
 * });
 * </pre>
 * 
 * @see <a href="http://www.w3.org/TR/webdatabase/#sql">W3C Web Database</a>
 * @see <a
 *      href="http://developer.apple.com/safari/library/documentation/iPhone/Conceptual/SafariJSDatabaseGuide/RelationalDatabases/chapter_4_section_3.html#//apple_ref/doc/uid/TP40007256-CH5-SW10">Safari
 *      Client-Side Storage - SQL Basics</a>
 * @see <a
 *      href="http://code.google.com/p/gwt-mobile-webkit/wiki/DatabaseApi">Wiki
 *      - Quickstart Guide</a>
 * @author bguijt
 */
public final class Database extends JavaScriptObject {

  private static final DatabaseImpl impl = GWT.create(DatabaseImpl.class);

  protected Database() {
  }

  /**
   * Returns <code>true</code> if the Database API is supported on the running
   * platform.
   */
  public static boolean isSupported() {
    return impl.isSupported();
  }

  /**
   * Creates a handle to a Database instance.
   * 
   * @see <a href="http://www.w3.org/TR/webdatabase/#dom-opendatabase">W3C Web
   *      Database - openDatabase</a>
   * @param shortName (Short) name of the database
   * @param version Version of the database
   * @param displayName Descriptive name of the database
   * @param maxSizeBytes The estimated number of bytes reserved for the database
   * @return a Database instance
   * @throws DatabaseException if the User Agent disallows Database access, or
   *           if the version is incorrect
   */
  public static Database openDatabase(String shortName, String version,
      String displayName, int maxSizeBytes) throws DatabaseException {
    return impl.openDatabase(shortName, version, displayName, maxSizeBytes);
  }

  /**
   * Performs a SQL transaction on the Database in read/write mode.
   * 
   * @param callback the callback implementing the transaction script
   * 
   * @see <a
   *      href="http://www.w3.org/TR/webdatabase/#dom-database-transaction">W3C
   *      Web Database - transaction</a>
   */
  public void transaction(TransactionCallback callback) {
    impl.transaction(this, callback);
  }

  /**
   * Performs a SQL transaction on the Database in read mode.
   * 
   * @param callback the callback implementing the transaction script
   * 
   * @see <a
   *      href="http://www.w3.org/TR/webdatabase/#dom-database-readtransaction">W3C
   *      Web Database - readTransaction</a>
   */
  public void readTransaction(TransactionCallback callback) {
    impl.readTransaction(this, callback);
  }

  /**
   * Updates the version of the Database.
   * 
   * @see <a
   *      href="http://www.w3.org/TR/webdatabase/#dom-database-changeversion">W3C
   *      Web Database - changeVersion</a>
   */
  public void changeVersion(String oldVersion, String newVersion,
      TransactionCallback callback) {
    impl.changeVersion(this, oldVersion, newVersion, callback);
  }

  /**
   * @return the current version of the Database (as opposed to the expected
   *         version of the {@link Database} object).
   * @see <a href="http://www.w3.org/TR/webdatabase/#dom-database-version">W3C
   *      Web Database - getVersion</a>
   */
  public String getVersion() {
    return impl.getVersion(this);
  }
}
