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

import com.google.code.gwt.database.client.SQLResultSet;

/**
 * Provides the SQL update statement to be executed when the annotated method is
 * called. This annotation mandates a read/write transaction.
 * 
 * <p>
 * Provide any statement parameters between curly braces, e.g.:
 * </p>
 * 
 * <pre>
 * &#x40;Update("INSERT INTO mytable (when, name) VALUES(<b>{when.getTime()}</b>, <b>{name}</b>)")
 * void insertData(Date <b>when</b>, String <b>name</b>, VoidCallback callback);
 * </pre>
 * 
 * <h3>Collection input parameters</h3>
 * <p>
 * The SQL annotation can also be used to repeat a SQL statement for each item
 * in an {@link Iterable} collection or an Array. This mechanism is expressed
 * like this:
 * </p>
 * 
 * <pre>
 * &#x40;Update(sql="INSERT INTO mytable (when, name) VALUES (<b>{_.getTime()}</b>, {name})",
 *     foreach="<b>dates</b>")
 * void insertData(Iterable&lt;Date&gt; <b>dates</b>, VoidCallback callback);
 * </pre>
 * 
 * <p>
 * The important parts are emphasized in <b>bold</b>. The above specification is
 * translated to Java like this:
 * </p>
 * 
 * <pre>
 * for (Date <b>_</b> : <b>dates</b>) {
 *   tx.executeSql("INSERT INTO mytable (when, name) VALUES (<b>?</b>, ?)",
 *       new Object[] {<b>_.getTime()</b>, name});
 * }
 * </pre>
 * 
 * <h3>Return values</h3>
 * <p>
 * An INSERT statement returns information about the
 * {@link SQLResultSet#getInsertId() ID of an inserted record}. Use the
 * {@link RowIdListCallback} callback to get the inserted ROWID's:
 * </p>
 * 
 * <pre>
 * &#x40;Update(sql="INSERT INTO mytable (when, name) VALUES (<b>{_.getTime()}</b>, {name})",
 *     foreach="<b>dates</b>")
 * void insertData(Iterable&lt;Date&gt; <b>dates</b>, RowIdsCallback callback);
 * </pre>
 * 
 * <p>
 * The iterated statements are all executed within the same database
 * transaction.
 * </p>
 * 
 * <h3>SQL dialect</h3>
 * 
 * <p>
 * Up to now (oct. 2009) all HTML5 Database implementations use SQLite, which
 * has its own <a href="http://www.sqlite.org/lang.html">SQL flavor</a>.
 * </p>
 * 
 * @see <a href="http://www.sqlite.org/lang.html">SQLite3 SQL Language
 *      Reference</a>
 * @author bguijt
 */
@Documented
@Target(ElementType.METHOD)
public @interface Update {

  /**
   * Represents the SQL statement to execute. Either this attribute or
   * {@link #sql()} must be specified.
   */
  String value() default "";

  /**
   * Represents the SQL statement to execute. Either this attribute or
   * {@link #value()} must be specified.
   */
  String sql() default "";

  /**
   * Represents a Collection of items, over which the service method will
   * iterate. This attribute is optional.
   */
  String foreach() default "";
}
