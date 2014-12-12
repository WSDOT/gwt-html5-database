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

import com.google.code.gwt.database.client.service.VoidCallback;
import com.google.code.gwt.database.client.service.callback.voyd.StatementCallbackVoidCallback;
import com.google.code.gwt.database.client.service.callback.voyd.TransactionCallbackVoidCallback;
import com.google.code.gwt.database.client.util.StringUtils;
import com.google.gwt.core.ext.UnableToCompleteException;

/**
 * Represents a ServiceMethodCreator for the {@link VoidCallback} type.
 * 
 * @author bguijt
 */
public class ServiceMethodCreatorVoidCallback extends ServiceMethodCreator {

  @Override
  protected String getTransactionCallbackClassName()
      throws UnableToCompleteException {
    return genUtils.getClassName(TransactionCallbackVoidCallback.class);
  }

  @Override
  public void generateOnTransactionStartBody() throws UnableToCompleteException {
    if (StringUtils.isNotEmpty(foreach)) {
      generateExecuteIteratedSqlStatements();
    } else {
      generateExecuteSqlStatement();
    }
  }

  @Override
  protected void generateStatementCallbackParameter() {
    sw.print(", new "
        + genUtils.getClassName(StatementCallbackVoidCallback.class) + "(this)");
  }
}
