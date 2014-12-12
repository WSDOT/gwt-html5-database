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

import java.lang.annotation.Annotation;

import com.google.code.gwt.database.client.service.Connection;
import com.google.code.gwt.database.client.service.DataService;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

/**
 * Generates the proxy code for the {@link DataService} definitions.
 * 
 * <p>
 * To use this Generator, you need a GWT module with the following XML
 * configuration:
 * </p>
 * 
 * <pre>
 * &lt;generate-with class="com.google.code.gwt.database.rebind.DataServiceGenerator"&gt;
 *   &lt;when-type-assignable class="com.google.code.gwt.database.client.service.DataService" /&gt;
 * &lt;/generate-with&gt;
 * </pre>
 * 
 * <p>
 * The <code>Html5Database</code> module is already configured to use this
 * Generator.
 * </p>
 * 
 * @see DataService
 * 
 * @author bguijt
 */
public class DataServiceGenerator extends Generator {

  @Override
  public String generate(TreeLogger logger, GeneratorContext context,
      String requestedClass) throws UnableToCompleteException {

    // Assertions:

    TypeOracle typeOracle = context.getTypeOracle();
    assert (typeOracle != null);

    JClassType dataService = typeOracle.findType(requestedClass);
    if (dataService == null) {
      logger.log(TreeLogger.ERROR, "Unable to find metadata for type '"
          + requestedClass + "'", null);
      throw new UnableToCompleteException();
    }

    if (dataService.isInterface() == null) {
      logger.log(TreeLogger.ERROR, dataService.getQualifiedSourceName()
          + " is not an interface", null);
      throw new UnableToCompleteException();
    }

    Connection conAnnotation = getAnnotation(dataService, Connection.class);
    if (conAnnotation == null) {
      logger.log(TreeLogger.ERROR,
          "DataService interface (or the interface it is implementing) must be annotated"
              + " with @Connection to define database connection details");
      throw new UnableToCompleteException();
    }

    // All basic assertions checked: Generate the code!

    SqlProxyCreator creator = new SqlProxyCreator(logger.branch(
        TreeLogger.DEBUG, "Generating proxy methods to database '"
            + conAnnotation.name() + "'..."), context, dataService);
    return creator.create();
  }

  /**
   * Finds the specified annotation on the inheritance tree of the specified
   * type.
   */
  private <T extends Annotation> T getAnnotation(JClassType type, Class<T> clazz) {
    T a = type.getAnnotation(clazz);
    if (a != null) {
      return a;
    }
    JClassType[] implemented = type.getImplementedInterfaces();
    if (implemented != null && implemented.length > 0) {
      return getAnnotation(implemented[0], clazz);
    }
    return null;
  }
}
