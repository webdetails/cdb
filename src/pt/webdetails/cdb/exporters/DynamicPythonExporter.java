/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.exporters;

/**
 *
 * @author pdpi
 */
public class DynamicPythonExporter extends AbstractExporter {

    public DynamicPythonExporter() {
    this.fileExportExtension = "py";
  }
    
  static final private String LOAD_CDB = "def readCdb(url, group, id):\n"
          + "  import csv, getpass, urllib\n"
          + "  user = \"&userid=\" + urllib.quote(raw_input(\"User: \"))\n"
          + "  password = \"&password=\" + urllib.quote(getpass.getpass())\n"
          + "  base_path = \"/pentaho/content/cdb/doQuery?outputType=csv\"\n"
          + "  file = \"&group=\" + urllib.quote(group)\n"
          + "  data_id = \"&id=\" + urllib.quote(id)\n"
          + "  complete_url = url + base_path + file + data_id + user + password\n"
          + "  print complete_url\n"
          + "  csv_file = open(urllib.urlretrieve(complete_url)[0])\n"
          + "  dialect = csv.Sniffer().sniff(csv_file.read(1024))\n"
          + "  csv_file.seek(0)\n"
          + "  return csv.reader(csv_file, dialect)\n"
          + "\n";

  @Override
  public String export(String group, String id, String url) {
    String loadString = "cdbData = readCdb(\"" + url + "\", \"" + group + "\", \"" + id + "\");\n";
    return LOAD_CDB + loadString;
  }

}
