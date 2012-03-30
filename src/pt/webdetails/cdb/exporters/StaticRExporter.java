/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.exporters;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author pdpi
 */
public class StaticRExporter extends AbstractExporter {

  @Override
  public String export(String group, String id, String url) {
    return "";
  }

  @Override
  public void binaryExport(String group, String id, String url, OutputStream out) throws IOException {
    String code = "cdbData <- read.csv2(\"" + id + ".csv\")\n";
    ZipOutputStream zos =  new ZipOutputStream(out);
    zos.putNextEntry(new ZipEntry(group + "/"));
    zos.putNextEntry(new ZipEntry(group + "/" + id + ".R"));
    zos.write(code.getBytes("utf-8"));
    zos.putNextEntry(new ZipEntry(group + "/" + id + ".csv"));
    zos.write(ExporterEngine.exportCda(group, id, "csv").getBytes("utf-8"));
    zos.close();
  }

}
