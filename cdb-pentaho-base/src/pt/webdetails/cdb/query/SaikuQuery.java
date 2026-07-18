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
import pt.webdetails.cda.connections.mondrian.JndiConnection;
import pt.webdetails.cda.connections.mondrian.MondrianJndiConnectionInfo;
import pt.webdetails.cda.dataaccess.DataAccess;
import pt.webdetails.cda.dataaccess.MdxDataAccess;
import pt.webdetails.cdb.util.CdbEnvironment;
import pt.webdetails.cpf.Util;

public class SaikuQuery extends AbstractQuery {

  private static final Log logger = LogFactory.getLog( pt.webdetails.cdb.query.SaikuQuery.class );
  private static final String path = "saiku";

  @Override
  public DataAccess exportCdaDataAccess() {
    String id, name, queryContent;
    id = getName();
    name = id;
    queryContent = getProperty( "query" ).toString();
    DataAccess dataAccess = new MdxDataAccess( id, name, id, queryContent );
    //dataAccess;
    return dataAccess;
  }

  @Override
  public Connection exportCdaConnection() {
    String jndi, cube, catalog;
    jndi = getProperty( "jndi" ).toString();
    cube = getProperty( "cube" ).toString();
    catalog = getProperty( "catalog" ).toString();

    MondrianJndiConnectionInfo cinfo = new MondrianJndiConnectionInfo( jndi, catalog, cube );
    Connection conn = new JndiConnection( getName(), cinfo );
    return conn;
  }

  @Override
  public void copy( String newGuid ) {
    String newFileName = newGuid + ".saiku",
      oldFileName = getId() + ".saiku";

    CdbEnvironment.getPluginRepositoryWriter()
      .copyFile( Util.joinPath( path, oldFileName ), Util.joinPath( path, newFileName ) );
  }

  @Override
  public void delete() {
    String fileName = getId() + ".saiku";
    CdbEnvironment.getPluginRepositoryWriter().deleteFile( path + "/" + fileName );
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
