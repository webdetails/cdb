/*!
* Copyright 2002 - 2013 Webdetails, a Pentaho company. All rights reserved.
*
* This software was developed by Webdetails and is provided under the terms
* of the Mozilla Public License, Version 2.0, or any later version. You may not use
* this file except in compliance with the license. If you need a copy of the license,
* please go to http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
*
* Software distributed under the Mozilla Public License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. Please refer to
* the license for the specific language governing your rights and limitations.
*/
package pt.webdetails.cdb;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.platform.api.engine.IPluginLifecycleListener;
import org.pentaho.platform.api.engine.PluginLifecycleException;
import pt.webdetails.cpf.PluginEnvironment;
import pt.webdetails.cpf.SimpleLifeCycleListener;

public class CdbLifeCycleListener extends SimpleLifeCycleListener implements IPluginLifecycleListener {

  static Log logger = LogFactory.getLog( CdbLifeCycleListener.class );

  @Override
  public void init() throws PluginLifecycleException {
    //super.init();
    logger.debug("Init for CDB");
  }

  @Override
  public void loaded() throws PluginLifecycleException {
    //super.loaded();
    logger.debug("Load for CDB");
  }

  @Override
  public PluginEnvironment getEnvironment() {
    return (PluginEnvironment) CdbEngine.getInstance().getEnvironment();
  }
}
