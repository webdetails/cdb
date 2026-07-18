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
      PluginIOUtils.writeOutAndFlush( response.getOutputStream(), result.toString( 2 ) );

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
      PluginIOUtils.writeOutAndFlush( response.getOutputStream(), result.toString( 2 ) );

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
