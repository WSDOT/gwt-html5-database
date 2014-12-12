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

package com.google.code.gwt.database.client.util;

import java.util.List;

import com.google.code.gwt.database.rebind.DataServiceGenerator;

/**
 * Provides several String manipulation routines used by the
 * {@link DataServiceGenerator} and some client classes.
 * 
 * @author bguijt
 */
public class StringUtils {

  public static boolean isEmpty(String s) {
    return s == null || s.trim().length() == 0;
  }

  public static boolean isNotEmpty(String s) {
    return !isEmpty(s);
  }

  /**
   * Returns the escaped String literal of the specified String <code>s</code>.
   */
  public static String getEscapedString(String s) {
    StringBuilder sb = new StringBuilder("\"");
    for (int i = 0; i < s.length(); i++) {
      sb.append(getEscapedChar(s.charAt(i)));
    }
    return sb.append("\"").toString();
  }

  /**
   * Returns the escaped String literal of the specified String <code>s</code>.
   * The returned String does *not* start and end with a quote (
   * <code>&quot;</code>) symbol!
   */
  public static String getEscapedStringPart(String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      sb.append(getEscapedChar(s.charAt(i)));
    }
    return sb.toString();
  }

  /**
   * Returns the specified char <code>ch</code> as a String literal to the
   * specified StringBuilder <code>sb</code>. This is for escaping Java string
   * literals.
   */
  public static String getEscapedChar(char ch) {
    switch (ch) {
      case '"':
        return "\\\"";
      case '\\':
        return "\\\\";
      case '\n':
        return "\\\n";
      case '\r':
        return "\\\r";
      case '\t':
        return "\\\t";
      default:
        return String.valueOf(ch);
    }
  }

  /**
   * Joins the values of the array together separated by the join argument.
   */
  public static String join(Object[] values, String join) {
    if (values == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < values.length; i++) {
      if (i > 0) {
        sb.append(join);
      }
      sb.append(values[i]);
    }
    return sb.toString();
  }

  public static String join(List<?> values, String join) {
    if (values == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < values.size(); i++) {
      if (i > 0) {
        sb.append(join);
      }
      sb.append(values.get(i));
    }
    return sb.toString();
  }
}
