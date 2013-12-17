/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.exporters;

import pt.webdetails.cpf.utils.CharsetHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author pdpi
 */
public class StaticPythonExporter extends AbstractExporter {

  public StaticPythonExporter() {
    this.fileExportExtension = "zip";
    this.templateFile = "StaticPython.mustache";
  }

  @Override
  public void binaryExport(String group, String id, String url, OutputStream out) throws IOException {
    String src = getSource(group, id, url);
    ZipOutputStream zos = new ZipOutputStream(out);
    zos.putNextEntry(new ZipEntry(id + ".py"));
    zos.write(src.getBytes( CharsetHelper.getEncoding() ));
    zos.putNextEntry(new ZipEntry(id + ".csv"));
    zos.write(ExporterEngine.exportCda(group, id, "csv").getBytes( CharsetHelper.getEncoding() ));
    zos.close();
  }

}
