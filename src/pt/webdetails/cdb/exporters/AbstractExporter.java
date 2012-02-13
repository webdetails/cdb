/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.exporters;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 *
 * @author pdpi
 */
public abstract class AbstractExporter implements Exporter {

  AbstractExporter() {
  }

  @Override
  public String export(String group, String id, String url, Map<String, String> params) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void binaryExport(String group, String id, String url, Map<String, String> params, OutputStream out) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void binaryExport(String group, String id, String url, OutputStream out) throws IOException {
    out.write(export(group, id, url).getBytes("utf-8"));
  }

}
