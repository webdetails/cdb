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


package pt.webdetails.cdb.api;

import pt.webdetails.cdb.CdbEngine;
import pt.webdetails.cdb.connector.ConnectorEngine;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path( "/cdb/api/connector" )
public class ConnectorApi {

  @GET
  @Path( "/exportCda" )
  public void exportCda( @QueryParam( MethodParams.GROUP ) String group ) {
    CdbEngine.getEnv(); // TODO: FOR REMOVE WHEN FOUND A WAY TO INSTANTIATE IN LYFECYCLE GIVES AN ERROR
    ConnectorEngine.getInstance();
    ConnectorEngine.exportCda( group );
  }

  @GET
  @Path( "/copyQuery" )
  public void copyQuery( @QueryParam( MethodParams.ID ) String id,
                         @QueryParam( MethodParams.NEW_GUID ) String newguid ) {
    CdbEngine.getEnv(); // TODO: FOR REMOVE WHEN FOUND A WAY TO INSTANTIATE IN LYFECYCLE GIVES AN ERROR
    ConnectorEngine.getInstance().copyQuery( id, newguid );
  }

  @GET
  @Path( "/deleteQuery" )
  public void deleteQuery( @QueryParam( MethodParams.ID ) String id ) {
    CdbEngine.getEnv(); // TODO: FOR REMOVE WHEN FOUND A WAY TO INSTANTIATE IN LYFECYCLE GIVES AN ERROR
    ConnectorEngine.getInstance().deleteQuery( id );
  }

  private class MethodParams {
    public static final String GROUP = "group";
    public static final String ID = "id";
    public static final String NEW_GUID = "newguid";
  }
}
