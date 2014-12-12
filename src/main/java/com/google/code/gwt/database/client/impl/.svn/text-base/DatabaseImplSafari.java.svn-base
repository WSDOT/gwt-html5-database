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
import com.google.code.gwt.database.client.TransactionCallback;

/**
 * Safari-specific implementation for {@link Database}.
 * 
 * <p>
 * The main difference between the Safari implementation and the <a
 * href="http://www.w3.org/TR/webdatabase/#database">W3C Web Database</a>
 * recommendation is the lack of a readTransaction() method. Therefore, this
 * implementation simply calls
 * {@link #transaction(Database, TransactionCallback)} for <em>all</em> read and
 * read/write transactions.
 * </p>
 * 
 * @see <a href="http://www.w3.org/TR/webdatabase/#database">W3C Web Database</a>
 * @see <a
 *      href="https://developer.apple.com/safari/library/documentation/AppleApplications/Reference/WebKitDOMRef/Database_idl/Classes/Database/index.html">WebKit
 *      DOM Reference - Database</a>
 * @see <a
 *      href="http://developer.apple.com/documentation/iPhone/Conceptual/SafariJSDatabaseGuide/">Safari
 *      JavaScript Database Programming Guide</a>
 * @author bguijt
 */
public class DatabaseImplSafari extends DatabaseImpl {

  protected DatabaseImplSafari() {
  }

  @Override
  public void readTransaction(Database db, TransactionCallback callback) {
    transaction(db, callback);
  }
}
