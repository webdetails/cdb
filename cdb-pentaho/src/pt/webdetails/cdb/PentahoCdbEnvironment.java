/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2002 - 2026 by Pentaho Canada Inc. : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2030-06-15
 ******************************************************************************/


package pt.webdetails.cdb;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pt.webdetails.cda.CdaEngine;
import pt.webdetails.cda.PentahoCdaEnvironment;
import pt.webdetails.cdb.bean.factory.ICdbBeanFactory;
import pt.webdetails.cpf.PentahoPluginEnvironment;
import pt.webdetails.cpf.exceptions.InitializationException;

public class PentahoCdbEnvironment extends PentahoPluginEnvironment implements ICdbEnvironment {

  protected static Log logger = LogFactory.getLog( PentahoCdbEnvironment.class );

  private static final String PLUGIN_REPOSITORY_DIR = "cdb";
  private static final String SYSTEM_DIR = "system";

  private ICdbBeanFactory factory;

  public void init( ICdbBeanFactory factory ) throws InitializationException {
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
