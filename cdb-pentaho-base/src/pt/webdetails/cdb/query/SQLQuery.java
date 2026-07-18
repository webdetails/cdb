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


package pt.webdetails.cdb.query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import pt.webdetails.cda.connections.Connection;
import pt.webdetails.cda.connections.sql.JndiConnection;
import pt.webdetails.cda.connections.sql.SqlJndiConnectionInfo;
import pt.webdetails.cda.dataaccess.DataAccess;
import pt.webdetails.cda.dataaccess.SqlDataAccess;

public class SQLQuery extends AbstractQuery {

  private static final Log logger = LogFactory.getLog( pt.webdetails.cdb.query.SQLQuery.class );
  //private static final String path = "cdb/queries";

  @Override
  public DataAccess exportCdaDataAccess() {
    String id, name, queryContent;
    id = getName();
    name = id;
    queryContent = getProperty( "query" ).toString();
    DataAccess dataAccess = new SqlDataAccess( id, name, id, queryContent );
    //dataAccess;
    return dataAccess;
  }

  @Override
  public Connection exportCdaConnection() {
    String jndi;
    jndi = getProperty( "jndi" ).toString();
    SqlJndiConnectionInfo cinfo = new SqlJndiConnectionInfo( jndi, null, null, null, null );
    Connection conn = new JndiConnection( getName(), cinfo );
    return conn;
  }

  @Override
  public void copy( String newGuid ) {
    /* No-op */
  }

  @Override
  public void delete() {
    /* No-op */
  }

  @Override
  public JSONObject toJSON() {
    JSONObject json = super.toJSON();
    return json;
  }

  @Override
  public void fromJSON( JSONObject json ) {
    super.fromJSON( json );
  }

}
