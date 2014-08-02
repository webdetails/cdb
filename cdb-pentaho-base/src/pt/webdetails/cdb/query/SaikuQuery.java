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
