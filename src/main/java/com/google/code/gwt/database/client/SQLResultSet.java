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

import com.google.code.gwt.database.client.impl.SQLResultSetRowListJso;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Represents the ResultSet as a result of an SQL Statement invoked by
 * {@link SQLTransaction#executeSql(String, Object[], StatementCallback)}.
 * 
 * @see <a href="http://www.w3.org/TR/webdatabase/#sqlresultset">W3C Web Database
 *      - SQLResultSet</a>
 * @author bguijt
 */
public class SQLResultSet<T extends JavaScriptObject> extends JavaScriptObject {

  protected SQLResultSet() {
  }

  /**
   * @return the row ID of the row that the SQLResultSet object's SQL statement
   *         inserted into the database, if the statement inserted a row. If the
   *         statement inserted multiple rows, the ID of the last row will be
   *         returned. If the statement did not insert a row, then this call
   *         raises a DatabaseException.
   */
  public final int getInsertId() {
    try {
      return getInsertId0();
    } catch (JavaScriptException e) {
      // INVALID_ACCESS_ERR
      throw new DatabaseException("Could not get insertId from SQLResultSet: " + e.getName(), e);
    }
  }

  private final native int getInsertId0() /*-{
    return this.insertId;
  }-*/;

  /**
   * @return the number of rows that were affected by the SQL statement. If the
   *         statement did not affected any rows, then the attribute returns
   *         zero. For SELECT statements, this returns zero (querying the
   *         database doesn't affect any rows).
   */
  public final native int getRowsAffected() /*-{
    return this.rowsAffected;
  }-*/;

  /**
   * @return a SQLResultSetRowList representing the rows returned, in the order
   *         returned by the database. If no rows were returned, then the object
   *         will be empty (its {@link SQLResultSetRowList#getLength() length}
   *         will be zero).
   */
  public final SQLResultSetRowList<T> getRows() {
    return new SQLResultSetRowList<T>(getRows0());
  }
  private final native SQLResultSetRowListJso<T> getRows0() /*-{
    return this.rows;
  }-*/;
}
