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

package pt.webdetails.cdb.exporters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pentaho.platform.api.engine.IParameterProvider;
import org.pentaho.platform.engine.core.system.PentahoRequestContextHolder;
import pt.webdetails.cdb.ExporterNotFoundException;
import pt.webdetails.cdb.util.CdbEnvironment;
import pt.webdetails.cdb.util.JsonUtils;
import pt.webdetails.cpf.InterPluginCall;
import pt.webdetails.cpf.repository.api.IReadAccess;

import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.OutputStream;

public class ExporterEngine {

  protected Log logger = LogFactory.getLog( pt.webdetails.cdb.exporters.ExporterEngine.class );
  private static pt.webdetails.cdb.exporters.ExporterEngine _instance;

  private ExporterEngine() {
  }

  public static pt.webdetails.cdb.exporters.ExporterEngine getInstance() {
    if ( _instance == null ) {
      _instance = new pt.webdetails.cdb.exporters.ExporterEngine();
    }
    return _instance;
  }

  public void exportToFile( HttpServletRequest request, HttpServletResponse response, String exporterName, String group,
                            String id ) throws IOException {
    ServletRequestWrapper wrapper = (ServletRequestWrapper) request;
    String url = wrapper.getScheme() + "://" + wrapper.getServerName() + ":" + wrapper.getServerPort()
      + PentahoRequestContextHolder.getRequestContext().getContextPath();

    Exporter exporter = null;
    try {
      exporter = getExporter( exporterName );
      response.setHeader( "content-disposition",
        "attachment; filename=\"" + escapePath( exporter.getFilename( group, id, url ) ) + "\"" );
      exporter.binaryExport( group, id, url, response.getOutputStream() );
    } catch ( Exception ex ) {
      logger.error( ex );
      JsonUtils.buildJsonResult( response.getOutputStream(), false,
        "Exception found: " + ex.getClass().getName() + " - " + ex.getMessage() );
    }

  }

  public void exportNoFile( HttpServletRequest request, HttpServletResponse response, String exporterName,
                            String group, String id ) throws IOException {

    String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
      + PentahoRequestContextHolder.getRequestContext().getContextPath();

    Exporter exporter = null;
    try {
      exporter = getExporter( exporterName );
      String export = exporter.export( group, id, url );
      JsonUtils.buildJsonResult( response.getOutputStream(), export != null, export );
    } catch ( Exception ex ) {
      logger.error( ex );
      JsonUtils.buildJsonResult( response.getOutputStream(), false,
        "Exception found: " + ex.getClass().getName() + " - " + ex.getMessage() );
    }
  }

  public void process( IParameterProvider requestParams, IParameterProvider pathParams, OutputStream out ) {
    try {

    } catch ( Exception ex ) {
      logger.error( ex );
      JsonUtils.buildJsonResult( out, false, "Exception found: " + ex.getClass().getName() + " - " + ex.getMessage() );
    }
  }

  private String escapePath( String path ) {
    return path.replaceAll( "\\\\", "\\\\" ).replaceAll( "\"", "\\\"" );
  }

  /*public void export(String exporterName, String group, String id, String url,
  OutputStream out) throws ExporterRuntimeException, ExporterNotFoundException {
    Exporter exporter = getExporter(exporterName);
    try {
      out.write(exporter.export(group, id, url).getBytes("utf-8"));
    } catch (Exception e) {
      logger.error(e);
    }
  }  */

  /*public void exportToFile(String exporterName, String group, String id, String url,
  OutputStream out) throws ExporterRuntimeException, ExporterNotFoundException {
    Exporter exporter = getExporter(exporterName);
    try {
      exporter.binaryExport(group, id, url, out);
    } catch (Exception e) {
      logger.error(e);
    }
  }    */

  public Exporter getExporter( String exporterName ) throws ExporterRuntimeException, ExporterNotFoundException {
    try {
      Document doc = getConfigFile();
      String exporterClassName = doc.selectSingleNode( "//exporter[@id='" + exporterName + "']/@class" ).getText();
      Class exporterClass = Class.forName( exporterClassName );
      return (Exporter) exporterClass.getConstructor().newInstance();
    } catch ( ClassNotFoundException e ) {
      throw new ExporterNotFoundException( e );
    } catch ( Exception e ) {
      throw new ExporterRuntimeException( e );
    }
  }

  public String listExporters() {
    JSONArray arr = new JSONArray();

    Document doc = getConfigFile();
    List<Node> exporters = doc.selectNodes( "//exporter" );
    for ( Node exporter : exporters ) {
      String id = exporter.selectSingleNode( "@id" ).getText();
      try {
        JSONObject jsonExporter = new JSONObject();
        JSONObject jsonModes = new JSONObject();
        jsonExporter.put( "id", id );
        jsonExporter.put( "label", exporter.selectSingleNode( "@label" ).getText() );
        List<Node> modes = exporter.selectNodes( ".//mode" );
        for ( Node mode : modes ) {
          jsonModes.put( mode.selectSingleNode( "@type" ).getText(), true );
        }
        jsonExporter.put( "modes", jsonModes );
        arr.put( jsonExporter );
      } catch ( JSONException e ) {
        logger.error( "Failed to list exporter " + id + ". Reason: " + e );
      }
    }
    try {
      return arr.toString( 2 );
    } catch ( JSONException e ) {
      return null;
    }
  }

  public static String exportCda( String group, String id, String outputType ) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put( "path", "public/cdb/queries/" + group + ".cda" );
    params.put( "dataAccessId", id );
    params.put( "outputType", outputType );

    InterPluginCall pluginCall = new InterPluginCall( InterPluginCall.CDA, "doQuery", params );
    return pluginCall.call();
  }

  private Document getConfigFile() {
    Document doc;
    try {
      IReadAccess read = CdbEnvironment.getPluginSystemReader();
      SAXReader reader = new SAXReader();
      doc = reader.read( read.getFileInputStream( "exporters.xml" ) );
    } catch ( IOException e ) {
      doc = null;
    } catch ( DocumentException e ) {
      doc = null;
    }
    return doc;
  }


}
