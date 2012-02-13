/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletRequestWrapper;
import org.pentaho.platform.api.engine.IParameterProvider;
import pt.webdetails.cdb.exporters.ExporterEngine;
import pt.webdetails.cdb.connector.ConnectorEngine;
import pt.webdetails.cpf.InterPluginComms;
import pt.webdetails.cpf.InvalidOperationException;
import pt.webdetails.cpf.SimpleContentGenerator;
import pt.webdetails.cpf.annotations.AccessLevel;
import pt.webdetails.cpf.annotations.Exposed;
import pt.webdetails.cpf.persistence.PersistenceEngine;

/**
 *
 * @author pdpi
 */
public class CdbContentGenerator extends SimpleContentGenerator {

  @Exposed(accessLevel = AccessLevel.PUBLIC)
  public void edit(OutputStream out) throws IOException {
    IParameterProvider pathParams = parameterProviders.get("path");
    ServletRequestWrapper wrapper = (ServletRequestWrapper) pathParams.getParameter("httprequest");
    String root = wrapper.getScheme() + "://" + wrapper.getServerName() + ":" + wrapper.getServerPort();

    Map<String, Object> params = new HashMap<String, Object>();
    params.put("solution", "system");
    params.put("path", "cdb/resources/editor/");
    params.put("file", "editor.wcdf");
    params.put("absolute", "true");
    params.put("root", root);
    out.write(InterPluginComms.callPlugin("pentaho-cdf-dd", "Render", params).getBytes("utf-8"));
  }

  @Exposed(accessLevel = AccessLevel.PUBLIC)
  public void storage(OutputStream out) {
    IParameterProvider pathParams = parameterProviders.get("path");
    IParameterProvider requestParams = parameterProviders.get("request");
    PersistenceEngine engine = PersistenceEngine.getInstance();
    try {
      out.write(engine.process(requestParams, userSession).getBytes("utf-8"));
    } catch (InvalidOperationException e) {
      logger.error(e);
    } catch (Exception e) {
      logger.error(e);
    }
  }

  @Exposed(accessLevel = AccessLevel.PUBLIC)
  public void export(OutputStream out) {
    IParameterProvider pathParams = parameterProviders.get("path");
    IParameterProvider requestParams = parameterProviders.get("request");
    ExporterEngine engine = ExporterEngine.getInstance();
    try {
      engine.process(requestParams, pathParams, out);
    } catch (Exception e) {
      logger.error(e);
    }
  }

  @Exposed(accessLevel = AccessLevel.PUBLIC)
  public void doquery(OutputStream out) throws IOException {
    IParameterProvider requestParams = parameterProviders.get("request");
    String group = requestParams.getStringParameter("group", ""),
            id = requestParams.getStringParameter("id", ""),
            outputType = requestParams.getStringParameter("outputType", "json");
    out.write(ExporterEngine.exportCda(group, id, outputType).getBytes("utf-8"));
  }

  @Exposed(accessLevel = AccessLevel.PUBLIC)
  public void crud(OutputStream out) throws IOException {
    IParameterProvider pathParams = parameterProviders.get("path");
    IParameterProvider requestParams = parameterProviders.get("request");
    String method = requestParams.getStringParameter("method", null);
    if ("new".equals(method)) {
      out.write(ConnectorEngine.getInstance().newDataAccess().getBytes("utf-8"));
    } else if ("edit".equals(method)) {
      out.write(ConnectorEngine.getInstance().editDataAccess().getBytes("utf-8"));
    } else if ("delete".equals(method)) {
      out.write(ConnectorEngine.getInstance().deleteDataAccess().getBytes("utf-8"));
    } else if ("view".equals(method)) {
      out.write(ConnectorEngine.getInstance().viewDataAccess().getBytes("utf-8"));
    }
  }

}
