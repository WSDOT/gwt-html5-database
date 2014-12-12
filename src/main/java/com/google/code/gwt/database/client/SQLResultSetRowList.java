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

import java.util.Iterator;

import com.google.code.gwt.database.client.impl.SQLResultSetRowListJso;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Represents a tabular resultset much like JDBC's {@link java.sql.ResultSet}.
 * 
 * <p>
 * The object type of the row returned by {@link #getItem(int)} is specified as
 * the generic type accompanying this class, which is eventually specified at
 * the {@link StatementCallback} interface. You can use {@link GenericRow} as a
 * dynamic object type.
 * </p>
 * 
 * @param <T> specified the type to represent a row in the ResultSet. It must be
 *          a subclass of {@link JavaScriptObject}. You could use
 *          {@link GenericRow} as a sensible default.
 * 
 * @see GenericRow
 * @see StatementCallback
 * @see <a href="http://www.w3.org/TR/webdatabase/#sqlresultsetrowlist">W3C Web
 *      Database - SQLResultSetRowList</a>
 * 
 * @author bguijt
 */
public final class SQLResultSetRowList<T extends JavaScriptObject> implements
    Iterable<T> {

  private SQLResultSetRowListJso<T> jso;

  SQLResultSetRowList(SQLResultSetRowListJso<T> jso) {
    this.jso = jso;
  }

  /**
   * @return the number of rows it represents (the number of rows returned by the database)
   */
  public int getLength() {
    return jso.getLength();
  }

  /**
   * Returns a ResultSet row.
   * 
   * @param index
   * @return the row with the given index index.
   * @exception DatabaseException if the given index is out of bounds
   */
  public T getItem(int index) {
    if (index >= 0 && index < getLength()) {
      return jso.getItem(index);
    }
    // INDEX_SIZE_ERR
    throw new DatabaseException("Index " + index + " out of bounds: size="
        + getLength());
  }

  /**
   * Returns an Iterator associated with the ResultSet rows.
   * 
   * <p>
   * Note: The {@link Iterator#remove()} operation is not supported.
   * </p>
   */
  public Iterator<T> iterator() {
    return new Iterator<T>() {
      private int index = -1;

      public boolean hasNext() {
        return index < getLength() - 1;
      }

      public T next() {
        index++;
        return getItem(index);
      }

      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }
}
