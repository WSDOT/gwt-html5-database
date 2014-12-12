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

package com.google.code.gwt.database.rebind;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JType;

/**
 * Provides context-scoped utility functions for a GWT generator.
 * 
 * @author bguijt
 */
public class GeneratorUtils {

  private TreeLogger logger;
  private GeneratorContext context;
  private String[] importedClasses;

  /**
   * Creates a new GeneratorUtils instance with the specified context
   * parameters.
   */
  public GeneratorUtils(TreeLogger logger, GeneratorContext context,
      String[] importedClasses) {
    this.logger = logger;
    this.context = context;
    this.importedClasses = importedClasses;
  }

  /**
   * Creates a new GeneratorUtils instance, with a branched TreeLogger.
   */
  public GeneratorUtils branchWithLogger(TreeLogger branchedLogger) {
    return new GeneratorUtils(branchedLogger, context, importedClasses);
  }

  /**
   * Returns the Type of the parameter with the specified name, or
   * <code>null</code> if not found.
   */
  public static JType findType(String name, JParameter[] parameters) {
    for (JParameter param : parameters) {
      if (param.getName().equals(name)) {
        return param.getType();
      }
    }
    return null;
  }

  /**
   * Returns the name of the specified type, which can be safely emitted in the
   * generated sourcecode.
   */
  public String getClassName(JType type) {
    if (type.isPrimitive() != null) {
      return type.isPrimitive().getSimpleSourceName();
    }

    StringBuilder sb = new StringBuilder(
        shortenName(type.getQualifiedSourceName()));

    if (type.isParameterized() != null
        && type.isParameterized().getTypeArgs().length > 0) {
      sb.append('<');
      boolean needComma = false;
      for (JType typeArg : type.isParameterized().getTypeArgs()) {
        if (needComma) {
          sb.append(", ");
        } else {
          needComma = true;
        }
        sb.append(shortenName(typeArg.getParameterizedQualifiedSourceName()));
      }
      sb.append('>');
    }

    return sb.toString();
  }

  /**
   * Returns the name of the specified clazz, which can be safely emitted in the
   * generated sourcecode.
   */
  public String getClassName(Class<?> clazz) {
    return shortenName(clazz.getCanonicalName());
  }

  /**
   * Returns the shortest name of the specified className.
   * 
   * <p>
   * The package part is removed if it is either <code>java.lang</code> or if
   * the class is part of the import list (see {@link #importedClasses}).
   * </p>
   */
  public String shortenName(String className) {
    int index = className.lastIndexOf('.');
    if (index == -1) {
      // No package name (primitive?)
      return className;
    }
    String packageName = className.substring(0, index);
    if ("java.lang".equals(packageName)) {
      return className.substring(index + 1);
    }
    for (String i : importedClasses) {
      if (i.equals(className)) {
        return className.substring(index + 1);
      }
    }
    return className;
  }

  /**
   * Returns the (first) Type parameter of the specified type.
   */
  public String getTypeParameter(JType type) throws UnableToCompleteException {
    JClassType[] typeArgs = type.isParameterized() != null
        ? type.isParameterized().getTypeArgs() : null;
    if (typeArgs == null || typeArgs.length == 0) {
      logger.log(TreeLogger.ERROR, "Expected a type parameter on the type "
          + type.getQualifiedSourceName());
      throw new UnableToCompleteException();
    }
    return shortenName(typeArgs[0].getQualifiedSourceName());
  }

  /**
   * Fabricates the name of a variable for use in a method body.
   * 
   * <p>
   * This method ensures that the returned name is not used as parameter name.
   * </p>
   */
  public static String getVariableName(String name, JParameter[] params) {
    for (JParameter param : params) {
      if (name.equals(param.getName())) {
        return getVariableName("_" + name, params);
      }
    }
    return name;
  }

  /**
   * Returns <code>true</code> if the specified types are the same.
   */
  public static boolean isType(JType type, Class<?> clazz) {
    return type.getQualifiedSourceName().equals(clazz.getCanonicalName());
  }

  /**
   * Returns <code>true</code> if the specified type is assignable to the
   * specified class <code>assignableTo</code>.
   */
  public boolean isAssignableToType(JType type, Class<?> assignableTo) {
    if (type.isClassOrInterface() != null) {
      return type.isClassOrInterface().isAssignableTo(
          context.getTypeOracle().findType(assignableTo.getCanonicalName()));
    }
    return false;
  }
}
