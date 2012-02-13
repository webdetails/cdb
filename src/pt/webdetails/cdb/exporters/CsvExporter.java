/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.exporters;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author pdpi
 */
public class CsvExporter extends AbstractExporter {

  @Override
  public String export(String group, String id, String url) {
    return ExporterEngine.exportCda(group, id, "csv");
  }


}
