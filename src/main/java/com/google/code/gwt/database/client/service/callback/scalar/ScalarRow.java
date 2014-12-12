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

package com.google.code.gwt.database.client.service.callback.scalar;

import com.google.code.gwt.database.rebind.DataServiceGenerator;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Used in the {@link DataServiceGenerator} to reduce generated boilerplate
 * code.
 * 
 * <p>
 * This Row type is used to obtain a scalar value from the resultSet.
 * </p>
 * 
 * @author bguijt
 */
public final class ScalarRow<T> extends JavaScriptObject {

  protected ScalarRow() {
  }

  public native byte getByte() /*-{
    for (var n in this) {
      return this[n];
    }
  }-*/;

  public native short getShort() /*-{
    for (var n in this) {
      return this[n];
    }
  }-*/;

  public native int getInteger() /*-{
    for (var n in this) {
      return this[n];
    }
  }-*/;

  public native float getFloat() /*-{
    for (var n in this) {
      return this[n];
    }
  }-*/;

  public native double getDouble() /*-{
    for (var n in this) {
      return this[n];
    }
  }-*/;

  public native boolean getBoolean() /*-{
    for (var n in this) {
      return this[n] ? true : false;
    }
  }-*/;

  public native String getString() /*-{
    for (var n in this) {
      return this[n];
    }
  }-*/;
}
