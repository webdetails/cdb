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

package pt.webdetails.cdb.connector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import pt.webdetails.cda.connections.Connection;
import pt.webdetails.cda.dataaccess.DataAccess;
import pt.webdetails.cda.settings.CdaSettings;
import pt.webdetails.cdb.query.Query;
import pt.webdetails.cdb.util.CdbEnvironment;
import pt.webdetails.cpf.persistence.PersistenceEngine;
import pt.webdetails.cpf.utils.CharsetHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ConnectorEngine {

  static ConnectorEngine instance;
  private static final Log logger = LogFactory.getLog( ConnectorEngine.class );

  private ConnectorEngine() {
  }

  public static ConnectorEngine getInstance() {
    if ( instance == null ) {
      instance = new ConnectorEngine();
    }
    return instance;
  }

  protected Connector getConnector( String connectorName )
    throws ConnectorRuntimeException, ConnectorNotFoundException {
    try {
      Class<?> connectorClass = Class.forName( "pt.webdetails.cdb.query." + connectorName + "Query" );
      return (Connector) connectorClass.getConstructor().newInstance();
    } catch ( ClassNotFoundException e ) {
      throw new ConnectorNotFoundException( e );
    } catch ( Exception e ) {
      throw new ConnectorRuntimeException( e );
    }
  }

  protected Query getQuery( String key ) throws ConnectorRuntimeException, ConnectorNotFoundException {
    PersistenceEngine eng = PersistenceEngine.getInstance();
    try {
      Map<String, Object> params = new HashMap<String, Object>();
      params.put( "id", key );
      JSONObject response = eng.query( "select * from Query where @rid = :id", params );
      JSONObject query = (JSONObject) ( (JSONArray) response.get( "object" ) ).get( 0 );
      return getQuery( query );
    } catch ( JSONException e ) {
      throw new ConnectorRuntimeException( e );
    }
  }

  protected Query getQuery( JSONObject json ) throws ConnectorRuntimeException, ConnectorNotFoundException {
    try {
      String type = json.getString( "type" );
      Class<?> queryClass = Class.forName( "pt.webdetails.cdb.query." + type + "Query" );
      Query q = (Query) queryClass.getConstructor().newInstance();
      q.fromJSON( json );
      return q;
    } catch ( ClassNotFoundException e ) {
      throw new ConnectorNotFoundException( e );
    } catch ( Exception e ) {
      throw new ConnectorRuntimeException( e );
    }
  }

  public String[] listConnectors() {
    String[] types = { "Saiku", "SQL" };
    return types;
  }

  public static void exportCda( String groupId ) {
    PersistenceEngine eng = PersistenceEngine.getInstance();
    try {

      Map<String, Object> params = new HashMap<String, Object>();
      params.put( "group", groupId );
      params.put( "user", PentahoSessionHolder.getSession().getName() );

      // DISABLING MULTI USER SUPPORT BY NOW JSONObject response = eng.query("select * from Query where group =
      // :group and userid = :user",params);
      JSONObject response = eng.query( "select * from Query where group = :group ", params );


      JSONArray queries = (JSONArray) response.get( "object" );
      CdaSettings cda = new CdaSettings( groupId, null );
      for ( int i = 0; i < queries.length(); i++ ) {
        JSONObject query = queries.getJSONObject( i );
        String type = query.getString( "type" );
        Query q = pt.webdetails.cdb.connector.ConnectorEngine.getInstance().getQuery( query );
        try {
          Connector conn = pt.webdetails.cdb.connector.ConnectorEngine.getInstance().getConnector( type );

          Connection connection = q.exportCdaConnection();
          if ( connection == null ) {
            logger.error( "Connection is null. Something is wrong with query "
              + query.getString( "name" ) + ". Ignoring." );
            continue;
          }
          cda.addConnection( connection );
          DataAccess dataAccess = q.exportCdaDataAccess();
          cda.addDataAccess( dataAccess );
        } catch ( ConnectorNotFoundException cnfe ) {
          logger.error( "Connector of type " + type + " not found.", cnfe );
        } catch ( ConnectorException e ) {
          logger.error( e );
        }
      }
      String fileName = PentahoSessionHolder.getSession().getName() + "." + groupId + ".cda";

      InputStream inputStream = new ByteArrayInputStream( cda.asXML().getBytes( CharsetHelper.getEncoding() ) );

      CdbEnvironment.getPluginRepositoryWriter( "queries" ).saveFile( fileName, inputStream );
    } catch ( Exception e ) {
      logger.error( "Exception while exporting CDA.", e );
    }
  }

  public void copyQuery( String id, String newGuid ) {
    PersistenceEngine eng = PersistenceEngine.getInstance();
    try {
      getQuery( id ).copy( newGuid );
    } catch ( Exception e ) {
      logger.error( e );
    }
  }

  public void deleteQuery( String id ) {
    PersistenceEngine eng = PersistenceEngine.getInstance();
    try {
      getQuery( id ).delete();
    } catch ( Exception e ) {
      logger.error( e );
    }
  }

}
