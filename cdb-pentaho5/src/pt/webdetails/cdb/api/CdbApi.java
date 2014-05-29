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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.platform.api.engine.IParameterProvider;
import org.pentaho.platform.api.engine.IPluginManager;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.web.http.api.resources.PluginResource;
import pt.webdetails.cdb.CdbEngine;
import pt.webdetails.cdb.InterPluginBroker;
import pt.webdetails.cdb.exporters.ExporterEngine;
import pt.webdetails.cdb.util.JsonUtils;
import pt.webdetails.cdb.utils.RestApiUtils;
import pt.webdetails.cpf.InvalidOperationException;
import pt.webdetails.cpf.persistence.PersistenceEngine;
import pt.webdetails.cpf.utils.CharsetHelper;
import pt.webdetails.cpf.utils.MimeTypes;
import pt.webdetails.cpf.utils.PluginIOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path( "/cdb/api" )
public class CdbApi {

  static Log logger = LogFactory.getLog( CdbApi.class );

  public static final String PLUGIN_NAME = "cdb";

  @GET
  @Path( "/ping" )
  public String ping() {
    logger.info( "pong" );
    return "pong";
  }

  @GET
  @Path( "/home" )
  @Produces( MimeTypes.HTML )
  public String home( @Context HttpServletRequest request, @Context HttpServletResponse response,
                      @Context HttpHeaders headers ) throws IOException {
    CdbEngine.getEnv(); // TODO: FOR REMOVE WHEN FOUND A WAY TO INSTANTIATE IN LYFECYCLE GIVES AN ERROR
    //IParameterProvider requestParams = getRequestParameters();
    // ServletRequest wrapper = getRequest();

    Map<String, Map<String, Object>> params = RestApiUtils.buildBloatedMap( request, response, headers );
    Map<String, Object> requestMap = new HashMap<String, Object>();
    Map<String, Object> requestParams = params.get( "request" );

    requestMap.put( "solution", "system" );
    requestMap.put( "path", "cdb/presentation" );
    requestMap.put( "file", "cdb.wcdf" );
    requestMap.put( "absolute", "false" );
    requestMap.put( "inferScheme", "false" );
    for ( String name : requestParams.keySet() ) {
      requestMap.put( name, requestParams.get( name ) );
    }
    //add request parameters
    //PluginUtils.copyParametersFromProvider(params, WrapperUtils.wrapParamProvider(requestParams));

    if ( requestMap.get( "mode" ) != null && requestMap.get( "mode" ).equals( "edit" ) ) {
      // Send this to CDE
      redirectToCdeEditor( response, requestMap );
      return "";
    }


    try {
      InterPluginBroker.run( requestMap, response.getOutputStream() );
    } catch ( Exception e ) {
      return e.toString();  //To change body of catch statement use File | Settings | File Templates.
    }

    /*InterPluginCall pluginCall = new InterPluginCall(InterPluginCall.CDE, "Render", params);
    pluginCall.setResponse(response);
    //pluginCall.setOutputStream(out);
    pluginCall.run();*/
    return "";
  }


  @GET
  @Path( "/storage" )
  @Produces( "text/javascript" )
  public void storage( @Context HttpServletRequest request, @Context HttpServletResponse response,
                       @Context HttpHeaders headers ) throws IOException, InvalidOperationException {
    CdbEngine.getEnv(); // TODO: FOR REMOVE WHEN FOUND A WAY TO INSTANTIATE IN LYFECYCLE GIVES AN ERROR
    Map<String, Map<String, Object>> bloatedMap = RestApiUtils.buildBloatedMap( request, response, headers );
    try {
      PersistenceEngine engine = PersistenceEngine.getInstance();
      String result =
        engine.process( RestApiUtils.getRequestParameters( bloatedMap ), PentahoSessionHolder.getSession() );
      PluginIOUtils.writeOutAndFlush( response.getOutputStream(), result );
    } catch ( Exception ex ) {
      logger.error( ex.toString() );
      JsonUtils.buildJsonResult( response.getOutputStream(), false,
        "Exception found: " + ex.getClass().getName() + " - " + ex.getMessage() );
    }
  }

  @GET
  @Path( "/doQuery" )
  @Produces( { "text/csv", "text/javascript" } )
  public void doQuery( @Context HttpServletRequest request, @Context HttpServletResponse response,
                       @Context HttpHeaders headers ) throws IOException {
    CdbEngine.getEnv(); // TODO: FOR REMOVE WHEN FOUND A WAY TO INSTANTIATE IN LYFECYCLE GIVES AN ERROR
    Map<String, Map<String, Object>> bloatedMap = RestApiUtils.buildBloatedMap( request, response, headers );

    IParameterProvider requestParams = RestApiUtils.getRequestParameters( bloatedMap );

    String group = requestParams.getStringParameter( "group", "" );
    String id = requestParams.getStringParameter( "id", "" );
    String outputType = requestParams.getStringParameter( "outputType", "json" );

    try {
      String result = ExporterEngine.exportCda( group, id, outputType );
      PluginIOUtils.writeOutAndFlush( response.getOutputStream(), result );
    } catch ( Exception ex ) {
      logger.error( ex.toString() );
      JsonUtils.buildJsonResult( response.getOutputStream(), false,
        "Exception found: " + ex.getClass().getName() + " - " + ex.getMessage() );
    }
  }

  private void redirectToCdeEditor( HttpServletResponse response,
                                    Map<String, Object> params ) throws IOException {

    StringBuilder urlBuilder = new StringBuilder();
    urlBuilder.append( "../../pentaho-cdf-dd/api/renderer/edit" );
    if ( params.size() > 0 ) {
      urlBuilder.append( "?" );
    }

    List<String> paramArray = new ArrayList<String>();
    for ( String key : params.keySet() ) {
      Object value = params.get( key );
      if ( value instanceof String ) {
        paramArray.add( key + "=" + URLEncoder.encode( (String) value, CharsetHelper.getEncoding() ) );
      }
    }

    urlBuilder.append( StringUtils.join( paramArray, "&" ) );

    if ( response == null ) {
      logger.error( "response not found" );
      return;
    }
    try {
      response.sendRedirect( urlBuilder.toString() );
    } catch ( IOException e ) {
      logger.error( "could not redirect", e );
    }
  }

  @GET
  @Path( "/{path: [^?]+ }" )
  @Produces( { MediaType.WILDCARD } )
  public Response getSystemResource( @PathParam( "path" ) String path, @Context HttpServletResponse response )
    throws IOException {

    String pluginId = CdbEngine.getInstance().getEnvironment().getPluginId();

    IPluginManager pluginManager = PentahoSystem.get( IPluginManager.class );

    if ( !StringUtils.isEmpty( path ) && pluginManager.isPublic( pluginId, path ) ) {

      Response readFileResponse = new PluginResource( response ).readFile( pluginId, path );

      if ( readFileResponse.getStatus() != Response.Status.NOT_FOUND.getStatusCode() ) {
        return readFileResponse;
      }
    }

    return Response.status( Response.Status.NOT_FOUND ).build();
  }
}
