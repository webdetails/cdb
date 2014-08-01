/*!
* Copyright 2002 - 2014 Webdetails, a Pentaho company.  All rights reserved.
*
* This software was developed by Webdetails and is provided under the terms
* of the Mozilla Public License, Version 2.0, or any later version. You may not use
* this file except in compliance with the license. If you need a copy of the license,
* please go to  http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
*
* Software distributed under the Mozilla Public License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
* the license for the specific language governing your rights and limitations.
*/

package pt.webdetails.cdb;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pt.webdetails.cda.CdaEngine;
import pt.webdetails.cda.PentahoCdaEnvironment;
import pt.webdetails.cdb.bean.factory.ICdbBeanFactory;
import pt.webdetails.cpf.PentahoPluginEnvironment;
import pt.webdetails.cpf.exceptions.InitializationException;
import pt.webdetails.cpf.persistence.PersistenceEngine;

public class PentahoCdbEnvironment extends PentahoPluginEnvironment implements ICdbEnvironment {

  protected static Log logger = LogFactory.getLog( PentahoCdbEnvironment.class );

  private static final String PLUGIN_REPOSITORY_DIR = "/public/cdb";
  private static final String SYSTEM_DIR = "system";

  private ICdbBeanFactory factory;

  public void init( ICdbBeanFactory factory ) throws InitializationException {
    PersistenceEngine.getInstance(); //Force Persistence engine initialization
    this.factory = factory;
    init( this );
    CdaEngine.init( new PentahoCdaEnvironment() );
  }

  public void refresh() {
    try {
      init( this.factory );
    } catch ( InitializationException e ) {
      logger.error( "PentahoCdbEnvironment.refresh()", e );
    }
  }

  @Override
  public String getPluginRepositoryDir() {
    return PLUGIN_REPOSITORY_DIR;
  }

  public String getSystemDir() {
    return SYSTEM_DIR;
  }

  @Override
  public String getPluginId() {
    return super.getPluginId();
  }


  public PentahoPluginEnvironment getPluginEnv() {
    return PentahoPluginEnvironment.getInstance();
  }
}
