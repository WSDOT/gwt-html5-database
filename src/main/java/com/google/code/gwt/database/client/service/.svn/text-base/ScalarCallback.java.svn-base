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

/**
 * Database service callback which expects a single item in the resultset.
 * 
 * @param <T> The expected data type from the single-record, single-column
 *          resultset. Basically, this must be a Number wrapper, a Date, or a
 *          String.
 * 
 * @author bguijt
 */
public interface ScalarCallback<T> extends Callback {

  /**
   * This callback method is invoked if the SQL is executed successfully.
   * 
   * <p>
   * The SQL is expected to return exactly one record and exactly one value.
   * </p>
   * 
   * @param result the result of the SQL query.
   */
  void onSuccess(T result);
}
