/*!
* Copyright 2002 - 2014 Webdetails, a Pentaho company. All rights reserved.
*
* This software was developed by Webdetails and is provided under the terms
* of the Mozilla Public License, Version 2.0, or any later version. You may not use
* this file except in compliance with the license. If you need a copy of the license,
* please go to http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
*
* Software distributed under the Mozilla Public License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. Please refer to
* the license for the specific language governing your rights and limitations.
*/

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
