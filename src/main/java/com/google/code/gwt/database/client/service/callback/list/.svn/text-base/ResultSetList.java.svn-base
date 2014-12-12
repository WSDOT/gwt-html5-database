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

import java.util.AbstractList;

import com.google.code.gwt.database.client.DatabaseException;
import com.google.code.gwt.database.client.SQLResultSet;
import com.google.code.gwt.database.client.SQLResultSetRowList;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * This List type is used to operate the database {@link SQLResultSet} as a
 * {@link java.util.List}.
 * 
 * <p>
 * This List is <em>immutable</em> - any operation modifying the List throws an
 * {@link UnsupportedOperationException}.
 * </p>
 * 
 * @param <T> the type used to represent a row in the resultset
 * 
 * @author bguijt
 */
public final class ResultSetList<T extends JavaScriptObject> extends
    AbstractList<T> {

  private SQLResultSetRowList<T> resultSet;

  /**
   * Creates a new List using the specified resultSet as data source.
   * 
   * <p>
   * No data is copied during instantiation, the resultSet is the sole data
   * source for this instance.
   * </p>
   */
  public ResultSetList(SQLResultSet<T> resultSet) {
    this.resultSet = resultSet.getRows();
  }

  @Override
  public T get(int index) {
    try {
      return resultSet.getItem(index);
    } catch (DatabaseException e) {
      // Make sure to satisfy the API contract:
      throw new IndexOutOfBoundsException(e.getMessage());
    }
  }

  /**
   * Returns the number of rows in the resultset, which is equal to the number
   * of elements in this List.
   */
  @Override
  public int size() {
    return resultSet.getLength();
  }
}
