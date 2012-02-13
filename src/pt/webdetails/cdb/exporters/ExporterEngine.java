/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.exporters;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.platform.api.engine.IParameterProvider;
import pt.webdetails.cdb.ExporterNotFoundException;
import pt.webdetails.cpf.InterPluginComms;

/**
 *
 * @author pdpi
 */
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
      ServletRequestWrapper wrapper = (ServletRequestWrapper) pathParams.getParameter("httprequest");
      String exporter = requestParams.getStringParameter("exporter", ""),
              group = requestParams.getStringParameter("group", ""),
              id = requestParams.getStringParameter("id", ""),
              filename = requestParams.getStringParameter("filename", "default"),
              url = wrapper.getScheme() + "://" + wrapper.getServerName() + ":" + wrapper.getServerPort();

      boolean toFile = requestParams.getStringParameter("toFile", "false").equals("true");
      if (toFile) {
        HttpServletResponse response = (HttpServletResponse) pathParams.getParameter("httpresponse");
        response.setHeader("content-disposition", "attachment; filename=" + filename);
        exportToFile(exporter, group, id, url, out);
      } else {
        export(exporter, group, id, url, out);
      }
      /*} catch (ExporterRuntimeException e) {
      logger.error(e);
      } catch (ExporterNotFoundException e) {
      logger.error(e);*/
    } catch (Exception e) {
      logger.error(e);
    }
  }

  public void export(String exporterName, String group, String id, String url, OutputStream out) throws ExporterRuntimeException, ExporterNotFoundException {
    Exporter exporter = getExporter(exporterName);
    try {
      out.write(exporter.export(group, id, url).getBytes("utf-8"));
    } catch (Exception e) {
      logger.error(e);
    }
  }

  public void exportToFile(String exporterName, String group, String id, String url, OutputStream out) throws ExporterRuntimeException, ExporterNotFoundException {
    Exporter exporter = getExporter(exporterName);
    try {
      exporter.binaryExport(group, id, url,out);
    } catch (Exception e) {
      logger.error(e);
    }
  }

  protected Exporter getExporter(String exporterName) throws ExporterRuntimeException, ExporterNotFoundException {
    try {
      Class exporterClass = Class.forName("pt.webdetails.cdb.exporters." + exporterName + "Exporter");
      return (Exporter) exporterClass.getConstructor().newInstance();
    } catch (ClassNotFoundException e) {
      throw new ExporterNotFoundException(e);
    } catch (Exception e) {
      throw new ExporterRuntimeException(e);
    }
  }

  public String[] listExporters() {
    String[] types = {"StaticR", "DynamicR", "Csv", "StaticPython", "DynamicPython"};
    return types;
  }

  public static String exportCda(String group, String id, String outputType) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("path", "cdb/queries/" + group + ".cda");
    params.put("dataAccessId", id);
    params.put("outputType", outputType);

    return InterPluginComms.callPlugin("cda", "doQuery", params);
  }

}
