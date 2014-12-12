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
 * Database service callback which expects no query results.
 * 
 * @author bguijt
 */
public interface VoidCallback extends Callback {

  /**
   * This callback method is invoked if the SQL is executed successfully.
   * 
   * <p>
   * Although the callback doesn't provide any resultset data, the SQL query
   * might return data but it will be ignored.
   * </p>
   */
  void onSuccess();
}
