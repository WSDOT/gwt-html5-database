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
 * All DataServiceXxxCallback interfaces mandate the same
 * {@link #onFailure(DataServiceException)} method, which is defined here.
 * 
 * <p>
 * You are NOT allowed to use this callback type in your DataService - use a
 * subtype insetad!
 * </p>
 * 
 * @author bguijt
 */
public interface Callback {

  /**
   * This callback method is invoked if the SQL transaction fails.
   * 
   * @param error the SQL error causing the failure
   */
  void onFailure(DataServiceException error);

}
