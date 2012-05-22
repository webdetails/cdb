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
public class StaticPythonExporter extends AbstractExporter {

  public StaticPythonExporter() {
    this.fileExportExtension = "zip";
  }

  @Override
  public String export(String group, String id, String url) {
    String src = "import csv, getpass, urllib\n"
            + "# Open the CSV file, detect the format (commas or semicolons? etc),\n"
            + "# rewind back to the start of the file, and parse it\n"
            + "csv_file = open(\"" + id + ".csv\")\n"
            + "dialect = csv.Sniffer().sniff(csv_file.read(1024))\n"
            + "csv_file.seek(0)\n"
            + "cdbData = csv.reader(csv_file, dialect)\n";
    return src;
  }

  @Override
  public void binaryExport(String group, String id, String url, OutputStream out) throws IOException {
    String src = "import csv, getpass, urllib\n"
            + "# Open the CSV file, detect the format (commas or semicolons? etc),\n"
            + "# rewind back to the start of the file, and parse it\n"
            + "csv_file = open(\"" + id + ".csv\")\n"
            + "dialect = csv.Sniffer().sniff(csv_file.read(1024))\n"
            + "csv_file.seek(0)\n"
            + "cdbData = csv.reader(csv_file, dialect)\n";
    ZipOutputStream zos = new ZipOutputStream(out);
    zos.putNextEntry(new ZipEntry(id + ".py"));
    zos.write(src.getBytes("utf-8"));
    zos.putNextEntry(new ZipEntry(id + ".csv"));
    zos.write(ExporterEngine.exportCda(group, id, "csv").getBytes("utf-8"));
    zos.close();
  }

}
