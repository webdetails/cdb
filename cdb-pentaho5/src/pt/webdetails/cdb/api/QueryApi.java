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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import pt.webdetails.cdb.CdbEngine;
import pt.webdetails.cdb.query.QueryEngine;
import pt.webdetails.cdb.util.JsonUtils;
import pt.webdetails.cpf.utils.PluginIOUtils;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.io.IOException;

@Path( "/cdb/api/query" )
public class QueryApi {

  static Log logger = LogFactory.getLog( QueryApi.class );

  @GET
  @Path( "/listGroups" )
  @Produces( { "application/json", "text/javascript" } )
  public void listGroups( @Context HttpServletResponse response ) throws IOException {
    CdbEngine.getEnv(); // TODO: FOR REMOVE WHEN FOUND A WAY TO INSTANTIATE IN LYFECYCLE GIVES AN ERROR
    try {
      JSONObject result = QueryEngine.getInstance().listGroups();
      String data = result.get("object").toString();

      JsonUtils.buildJsonResult( response.getOutputStream(), result != null, data );

    } catch ( Exception ex ) {
      logger.error( "Error listing queries: " + ex );
      JsonUtils.buildJsonResult( response.getOutputStream(), false,
        "Exception found: " + ex.getClass().getName() + " - " + ex.getMessage() );
    }
  }

  @GET
  @Path( "/loadGroup" )
  @Produces( { "application/json", "text/javascript" } )
  public void loadGroup( @QueryParam( MethodParams.GROUP ) String group, @Context HttpServletResponse response )
    throws IOException {
    CdbEngine.getEnv(); // TODO: FOR REMOVE WHEN FOUND A WAY TO INSTANTIATE IN LYFECYCLE GIVES AN ERROR
    try {
      JSONObject result = QueryEngine.getInstance().loadGroup( group );
      String data = result.get("object").toString();
      JsonUtils.buildJsonResult( response.getOutputStream(), result != null, data );
    } catch ( Exception ex ) {
      logger.error( "Error loading group: " + ex );
      JsonUtils.buildJsonResult( response.getOutputStream(), false,
        "Exception found: " + ex.getClass().getName() + " - " + ex.getMessage() );
    }
  }

  private class MethodParams {
    public static final String GROUP = "group";
  }
}
