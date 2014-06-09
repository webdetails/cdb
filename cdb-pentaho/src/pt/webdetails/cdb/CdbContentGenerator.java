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

package pt.webdetails.cdb;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.pentaho.platform.api.engine.IParameterProvider;
import org.pentaho.platform.engine.core.system.PentahoRequestContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.webdetails.cdb.connector.ConnectorEngine;
import pt.webdetails.cdb.exporters.Exporter;
import pt.webdetails.cdb.exporters.ExporterEngine;
import pt.webdetails.cdb.exporters.ExporterRuntimeException;
import pt.webdetails.cdb.query.QueryEngine;
import pt.webdetails.cdb.util.JsonUtils;
import pt.webdetails.cpf.InterPluginCall;
import pt.webdetails.cpf.InvalidOperationException;
import pt.webdetails.cpf.SimpleContentGenerator;
import pt.webdetails.cpf.WrapperUtils;
import pt.webdetails.cpf.annotations.AccessLevel;
import pt.webdetails.cpf.annotations.Exposed;
import pt.webdetails.cpf.olap.OlapUtils;
import pt.webdetails.cpf.persistence.PersistenceEngine;
import pt.webdetails.cpf.utils.PluginUtils;

import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CdbContentGenerator extends SimpleContentGenerator {

  private static final long serialVersionUID = 1L;

  private static Map<String, Method> exposedMethods = new HashMap<String, Method>();

  private Logger logger = LoggerFactory.getLogger( getClass() );

  static {
    //to keep case-insensitive methods
    exposedMethods = getExposedMethods( CdbContentGenerator.class, true );
  }

  @Override
  protected Method getMethod( String methodName ) throws NoSuchMethodException {
    Method method = exposedMethods.get( StringUtils.lowerCase( methodName ) );
    if ( method == null ) {
      throw new NoSuchMethodException();
    }
    return method;
  }

  @Override
  public String getPluginName() {
    return "cdb";
  }

  @Exposed( accessLevel = AccessLevel.PUBLIC )
  public void home( OutputStream out ) throws IOException {
    CdbEngine.getInstance().getEnvironment();
    IParameterProvider requestParams = getRequestParameters();

    Map<String, Object> params = new HashMap<String, Object>();
    params.put( "solution", "system" );
    params.put( "path", "cdb/presentation/" );
    params.put( "file", "cdb.wcdf" );
    params.put( "absolute", "false" );
    params.put( "inferScheme", "false" );


    //add request parameters
    PluginUtils.copyParametersFromProvider( params, WrapperUtils.wrapParamProvider( requestParams ) );

    if ( requestParams.hasParameter( "mode" ) && requestParams.getStringParameter( "mode", "Render" )
      .equals( "edit" ) ) {
      // Send this to CDE
      redirectToCdeEditor( params );
      return;
    }

    InterPluginCall pluginCall = new InterPluginCall( InterPluginCall.CDE, "Render", params );
    pluginCall.setResponse( getResponse() );
    pluginCall.setOutputStream( out );
    pluginCall.run();

  }

  @Exposed( accessLevel = AccessLevel.PUBLIC )
  public void storage( OutputStream out ) throws IOException, InvalidOperationException {
    PersistenceEngine engine = PersistenceEngine.getInstance();
    writeOut( out, engine.process( getRequestParameters(), userSession ) );
  }

  @Exposed( accessLevel = AccessLevel.PUBLIC )
  public void export( OutputStream out ) throws IOException, ExporterRuntimeException, ExporterNotFoundException {
    ExporterEngine engine = ExporterEngine.getInstance();
    IParameterProvider requestParams = getRequestParameters(),
      pathParams = getPathParameters();

    String method = requestParams.getStringParameter( "method", "" );

    if ( "listExporters".equals( method ) ) {
      String exporters = engine.listExporters();
      JsonUtils.buildJsonResult( out, exporters != null, exporters );
    } else if ( "export".equals( method ) ) {
      ServletRequestWrapper wrapper = (ServletRequestWrapper) pathParams.getParameter( "httprequest" );
      String exporterName = requestParams.getStringParameter( "exporter", "" ),
        group = requestParams.getStringParameter( "group", "" ),
        id = requestParams.getStringParameter( "id", "" ),
        url = wrapper.getScheme() + "://" + wrapper.getServerName() + ":" + wrapper.getServerPort()
          + PentahoRequestContextHolder.getRequestContext().getContextPath();

      boolean toFile = requestParams.getStringParameter( "toFile", "false" ).equals( "true" );
      Exporter exporter = engine.getExporter( exporterName );
      if ( toFile ) {
        HttpServletResponse response = (HttpServletResponse) pathParams.getParameter( "httpresponse" );
        String path = exporter.getFilename( group, id, url ).replaceAll( "\\\\", "\\\\" ).replaceAll( "\"", "\\\"" );
        response.setHeader( "content-disposition", "attachment; filename=\"" + path + "\"" );
        exporter.binaryExport( group, id, url, out );
      } else {
        String export = exporter.export( group, id, url );
        JsonUtils.buildJsonResult( out, export != null, export );
      }
    }
  }

  @Exposed( accessLevel = AccessLevel.PUBLIC )
  public void doQuery( OutputStream out ) throws IOException {
    IParameterProvider requestParams = getRequestParameters();

    String group = requestParams.getStringParameter( "group", "" );
    String id = requestParams.getStringParameter( "id", "" );
    String outputType = requestParams.getStringParameter( "outputType", "json" );

    writeOut( out, ExporterEngine.exportCda( group, id, outputType ) );
  }

  @Exposed( accessLevel = AccessLevel.PUBLIC )
  public void connector( OutputStream out ) throws IOException {
    ConnectorEngine engine = ConnectorEngine.getInstance();
    IParameterProvider requestParams = getRequestParameters();

    String method = requestParams.getStringParameter( MethodParams.METHOD, "" ),
      newGuid = requestParams.getStringParameter( MethodParams.NEW_GUID, "" ),
      group = requestParams.getStringParameter( MethodParams.GROUP, "" ),
      id = requestParams.getStringParameter( MethodParams.ID, "" );

    if ( "exportCda".equals( method ) ) {
      engine.exportCda( group );
    } else if ( "copyQuery".equals( method ) ) {
      engine.copyQuery( id, newGuid );
    } else if ( "deleteQuery".equals( method ) ) {
      engine.deleteQuery( id );
    } else {
      logger.error( "Unsupported method" );
    }
  }

  @Exposed( accessLevel = AccessLevel.PUBLIC )
  public void query( OutputStream out ) throws IOException {
    QueryEngine engine = QueryEngine.getInstance();
    IParameterProvider requestParams = getRequestParameters();

    String method = requestParams.getStringParameter( MethodParams.METHOD, "" ),
      group = requestParams.getStringParameter( MethodParams.GROUP, "" );

    if ( "listGroups".equals( method ) ) {
      try {
        out.write( engine.listGroups().toString( 2 ).getBytes( "utf-8" ) );
      } catch ( Exception e ) {
        logger.error( "Error listing queries: " + e );
      }
    } else if ( "loadGroup".equals( method ) ) {
      org.json.JSONObject response = engine.loadGroup( group );
      try {
        out.write( response.toString( 2 ).getBytes( "utf-8" ) );
      } catch ( Exception e ) {
        logger.error( "Error loading group: " + e );
      }
    }
  }

  @Exposed( accessLevel = AccessLevel.PUBLIC )
  public void olapUtils( OutputStream out ) throws IOException {
    OlapUtils olapUtils = new OlapUtils();
    JSONObject result = null;

    try {
      String operation = getRequestParameters().getStringParameter( "operation", "-" );

      if ( operation.equals( "GetOlapCubes" ) ) {

        result = olapUtils.getOlapCubes();

      } else if ( operation.equals( "GetCubeStructure" ) ) {

        String catalog = getRequestParameters().getStringParameter( "catalog", null );
        String cube = getRequestParameters().getStringParameter( "cube", null );
        String jndi = getRequestParameters().getStringParameter( "jndi", null );

        result = olapUtils.getCubeStructure( catalog, cube, jndi );

      } else if ( operation.equals( "GetLevelMembersStructure" ) ) {

        String catalog = getRequestParameters().getStringParameter( "catalog", null );
        String cube = getRequestParameters().getStringParameter( "cube", null );
        String member = getRequestParameters().getStringParameter( "member", null );
        String direction = getRequestParameters().getStringParameter( "direction", null );

        result = olapUtils.getLevelMembersStructure( catalog, cube, member, direction );

      }
      JsonUtils.buildJsonResult( out, result != null, result );
    } catch ( Exception ex ) {
      logger.error( ex.toString() );
      JsonUtils.buildJsonResult( out, false, "Exception found: " + ex.getClass().getName() + " - " + ex.getMessage() );
    }
  }

  private void redirectToCdeEditor( Map<String, Object> params ) throws IOException {

    StringBuilder urlBuilder = new StringBuilder();
    urlBuilder.append( "../pentaho-cdf-dd/edit" );
    if ( params.size() > 0 ) {
      urlBuilder.append( "?" );
    }

    List<String> paramArray = new ArrayList<String>();
    for ( String key : params.keySet() ) {
      Object value = params.get( key );
      if ( value instanceof String ) {
        paramArray.add( key + "=" + URLEncoder.encode( (String) value, getEncoding() ) );
      }
    }

    urlBuilder.append( StringUtils.join( paramArray, "&" ) );
    redirect( urlBuilder.toString() );
  }

  private class MethodParams {
    public static final String METHOD = "method";
    public static final String GROUP = "group";
    public static final String NEW_GUID = "newguid";
    public static final String ID = "id";

  }
}
