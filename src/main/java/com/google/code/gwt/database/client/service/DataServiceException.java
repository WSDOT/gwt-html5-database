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

import com.google.code.gwt.database.client.SQLError;
import com.google.code.gwt.database.client.util.StringUtils;

/**
 * Represents an exception in DataService failure handling.
 * 
 * @see SQLError
 * 
 * @author bguijt
 */
public class DataServiceException extends Exception {

  private static final long serialVersionUID = -3748774586186283062L;

  private int code;
  private String sql;
  private Object[] parameters;

  /**
   * Creates a DataServiceException using a SQLError as source.
   */
  public DataServiceException(SQLError error) {
    super(error.getMessage());
    this.code = error.getCode();
  }

  /**
   * Creates a DataServiceException from a custom message, with error code 0
   * (UNKNOWN_ERR).
   */
  public DataServiceException(String msg) {
    super(msg);
    this.code = 0;
  }

  /**
   * Creates a DataServiceException from a series of custom fields
   */
  public DataServiceException(String message, int code, String sql,
      Object[] parameters) {
    super(message);
    this.code = code;
    this.sql = sql;
    this.parameters = parameters;
  }

  /**
   * Returns the error code associated with the failure.
   * 
   * @see SQLError#getCode()
   */
  public int getCode() {
    return code;
  }

  /**
   * Returns the SQL statement which was executed when the error occurred.
   */
  public String getSql() {
    return sql;
  }

  /**
   * Returns the parameters of the SQL statement which was executed when the
   * error occurred.
   */
  public Object[] getParameters() {
    return parameters;
  }

  /**
   * Returns the error details in a single String, including the SQL query
   * statement and (if available) query parameters.
   */
  @Override
  public String toString() {
    return "DataServiceException: #" + code + " - " + getMessage()
        + " - Executed SQL: " + sql
        + (parameters != null
            ? ", args: [" + StringUtils.join(parameters, ", ") + "]"
            : "");
  }
}
