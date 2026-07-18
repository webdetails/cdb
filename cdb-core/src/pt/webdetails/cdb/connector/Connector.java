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


package pt.webdetails.cdb.connector;

import pt.webdetails.cda.dataaccess.DataAccess;
import pt.webdetails.cda.connections.Connection;

/**
 * @author pdpi
 */
public interface Connector {

  public DataAccess exportCdaDataAccess();

  public Connection exportCdaConnection();

  public void copy( String newGuid );

  public void delete();
}
