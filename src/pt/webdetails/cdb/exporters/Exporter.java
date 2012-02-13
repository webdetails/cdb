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
public interface Exporter {

  public String export(String group, String id, String url);

  public String export(String group, String id, String url, Map<String,String> params);

  public void binaryExport(String group, String id, String url, OutputStream out) throws IOException ;

  public void binaryExport(String group, String id, String url, Map<String,String> params, OutputStream out) throws IOException ;

}
