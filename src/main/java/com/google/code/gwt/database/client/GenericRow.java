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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

/**
 * Declares a generic row type, which could be returned by
 * {@link SQLResultSetRowList#getItem(int)} and specified by
 * {@link StatementCallback} and {@link SQLResultSet}.
 * 
 * @see SQLResultSet
 * @see SQLResultSetRowList
 * @see StatementCallback
 * @author bguijt
 */
public class GenericRow extends JavaScriptObject {

  protected GenericRow() {
  }

  public final native String getString(String name) /*-{
    return this[name];
  }-*/;

  public final native int getInt(String name) /*-{
    return this[name];
  }-*/;

  public final native float getFloat(String name) /*-{
    return this[name];
  }-*/;

  public final native double getDouble(String name) /*-{
    return this[name];
  }-*/;

  public final Date getDate(String name) {
    return new Date((long) getDouble(name));
  }

  public final native boolean getBoolean(String name) /*-{
    return this[name] ? true : false;
  }-*/;

  /**
   * @return a {@link List} of all attribute names in this Row.
   */
  public final List<String> getAttributeNames() {
    JsArrayString jas = getAttributeNames0();
    List<String> attributeNames = new ArrayList<String>(jas.length());
    for (int i = 0; i < jas.length(); i++) {
      attributeNames.add(jas.get(i));
    }
    return attributeNames;
  }

  private final native JsArrayString getAttributeNames0() /*-{
    var a = [];
    for (var n in this) {
      a.push(n);
    }
    return a;
  }-*/;
}
