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
import org.pentaho.platform.api.engine.PluginLifecycleException;
import pt.webdetails.cpf.PluginEnvironment;
import pt.webdetails.cpf.SimpleLifeCycleListener;

public class CdbLifeCycleListener extends SimpleLifeCycleListener {

  static Log logger = LogFactory.getLog( CdbLifeCycleListener.class );

  @Override
  public void init() throws PluginLifecycleException {
    logger.debug( "Init for CDB" );
  }

  @Override
  public void loaded() throws PluginLifecycleException {
    //getEnvironment();
    logger.debug( "Init for CDB" );
  }


  @Override
  public PluginEnvironment getEnvironment() {
    return (PluginEnvironment) CdbEngine.getInstance().getEnvironment();
  }

}
