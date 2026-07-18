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
public abstract class ConnectorException extends Exception {

  private static final long serialVersionUID = 1L;

  public ConnectorException() {
    super();
  }

  public ConnectorException( Exception cause ) {
    super( cause );
  }

}
