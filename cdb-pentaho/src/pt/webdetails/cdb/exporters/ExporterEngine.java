/*!
* Copyright 2002 - 2013 Webdetails, a Pentaho company. All rights reserved.
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
import pt.webdetails.cpf.InterPluginCall;
import pt.webdetails.cpf.repository.api.IReadAccess;
import pt.webdetails.cpf.utils.CharsetHelper;

import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExporterEngine {

  protected Log logger = LogFactory.getLog(ExporterEngine.class);
  private static ExporterEngine _instance;

  private ExporterEngine() {
  }

  public static ExporterEngine getInstance() {
    if (_instance == null) {
      _instance = new ExporterEngine();
    }
    return _instance;
  }

  public void process(IParameterProvider requestParams, IParameterProvider pathParams, OutputStream out) {
    try {
      String method = requestParams.getStringParameter("method", "");

      if ("listExporters".equals(method)) {
        String exporters = listExporters();
        out.write(exporters.getBytes( CharsetHelper.getEncoding() ));
      } else if ("export".equals(method)) {
        ServletRequestWrapper wrapper = (ServletRequestWrapper) pathParams.getParameter("httprequest");
        String exporterName = requestParams.getStringParameter("exporter", ""),
                group = requestParams.getStringParameter("group", ""),
                id = requestParams.getStringParameter("id", ""),
                filename = requestParams.getStringParameter("filename", "default"),
                url = wrapper.getScheme() + "://" + wrapper.getServerName() + ":" + wrapper.getServerPort() + PentahoRequestContextHolder.getRequestContext().getContextPath();

        boolean toFile = requestParams.getStringParameter("toFile", "false").equals("true");
        Exporter exporter = getExporter(exporterName);
        if (toFile) {
          HttpServletResponse response = (HttpServletResponse) pathParams.getParameter("httpresponse");
          response.setHeader("content-disposition", "attachment; filename=\"" + escapePath(exporter.getFilename(group, id, url)) + "\"") ;
          exporter.binaryExport(group, id, url, out);
        } else {
          out.write(exporter.export(group, id, url).getBytes(CharsetHelper.getEncoding()));
        }
        /*} catch (ExporterRuntimeException e) {
        logger.error(e);
        } catch (ExporterNotFoundException e) {
        logger.error(e);*/      }
    } catch (Exception e) {
      logger.error(e);
    }
  }

  private String escapePath(String path) {
  return path.replaceAll("\\\\", "\\\\").replaceAll("\"","\\\"");
  }
  public void export(String exporterName, String group, String id, String url, OutputStream out) throws ExporterRuntimeException, ExporterNotFoundException {
    Exporter exporter = getExporter(exporterName);
    try {
      out.write(exporter.export(group, id, url).getBytes( CharsetHelper.getEncoding() ));
    } catch (Exception e) {
      logger.error(e);
    }
  }

  public void exportToFile(String exporterName, String group, String id, String url, OutputStream out) throws ExporterRuntimeException, ExporterNotFoundException {
    Exporter exporter = getExporter(exporterName);
    try {
      exporter.binaryExport(group, id, url, out);
    } catch (Exception e) {
      logger.error(e);
    }
  }

  protected Exporter getExporter(String exporterName) throws ExporterRuntimeException, ExporterNotFoundException {
    try {
      Document doc = getConfigFile();
      String exporterClassName = doc.selectSingleNode("//exporter[@id='"+exporterName+"']/@class").getText();
      Class exporterClass = Class.forName(exporterClassName);
      return (Exporter) exporterClass.getConstructor().newInstance();
    } catch (ClassNotFoundException e) {
      throw new ExporterNotFoundException(e);
    } catch (Exception e) {
      throw new ExporterRuntimeException(e);
    }
  }

  public String listExporters() {
    JSONArray arr = new JSONArray();

    Document doc = getConfigFile();
    List<Node> exporters = doc.selectNodes("//exporter");
    for (Node exporter : exporters) {
      String id = exporter.selectSingleNode("@id").getText();
      try {
        JSONObject jsonExporter = new JSONObject();
        JSONObject jsonModes = new JSONObject();
        jsonExporter.put("id", id);
        jsonExporter.put("label", exporter.selectSingleNode("@label").getText());
        List<Node> modes = exporter.selectNodes(".//mode");
        for (Node mode : modes) {
          jsonModes.put(mode.selectSingleNode("@type").getText(), true);
        }
        jsonExporter.put("modes", jsonModes);
        arr.put(jsonExporter);
      } catch (JSONException e) {
        logger.error("Failed to list exporter " + id + ". Reason: " + e);
      }
    }
    try {
      return arr.toString(2);
    } catch (JSONException e) {
      return null;
    }

  }

  public static String exportCda(String group, String id, String outputType) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("path", "cdb/queries/" + group + ".cda");
    params.put("dataAccessId", id);
    params.put("outputType", outputType);

    InterPluginCall pluginCall = new InterPluginCall(InterPluginCall.CDA, "doQuery", params);
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