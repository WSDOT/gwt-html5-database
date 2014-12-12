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

package com.google.code.gwt.database.client.service;

import java.util.List;

import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.SQLResultSetRowList;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Database service callback which expects a collection of items in the
 * resultset.
 * 
 * @param <T> specified the type to represent a row in the ResultSet. It must be
 *          a subclass of {@link JavaScriptObject}. You could use
 *          {@link GenericRow} as a sensible default. See also
 *          {@link SQLResultSetRowList}.
 * 
 * @author bguijt
 */
public interface ListCallback<T extends JavaScriptObject> extends
    Callback {

  /**
   * This callback method is invoked if the SQL is executed successfully.
   * 
   * <p>The resultset is provided as a regular List collection.</p>
   * 
   * @param result the resultset provided as a List. Is never null, but might
   *          have zero items.
   */
  void onSuccess(List<T> result);
}
