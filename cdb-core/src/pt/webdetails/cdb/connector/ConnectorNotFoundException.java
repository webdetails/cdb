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

/**
 * @author pdpi
 */
public class ConnectorNotFoundException extends ConnectorException {

  public ConnectorNotFoundException() {
  }

  public ConnectorNotFoundException( Exception cause ) {
    super( cause );
  }

}
