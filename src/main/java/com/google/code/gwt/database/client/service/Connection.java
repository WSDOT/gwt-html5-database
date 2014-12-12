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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import com.google.code.gwt.database.client.Database;

/**
 * Marks a Java interface as a Database Service with connection parameters.
 * 
 * <p>
 * Use the parameters to specify the connection details to the local database.
 * The parameters are exactly the same as provided to
 * {@link Database#openDatabase(String, String, String, int)}.
 * </p>
 * 
 * @author bguijt
 */
@Documented
@Target(ElementType.TYPE)
public @interface Connection {

  /**
   * (Short) name of the database
   */
  String name();

  /**
   * Version of the database
   */
  String version();

  /**
   * Descriptive name of the database
   */
  String description();

  /**
   * The estimated number of bytes reserved for the database
   */
  int maxsize();
}
