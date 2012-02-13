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
public class DynamicRExporter extends AbstractExporter {

  static final private String LOAD_CDB = "readCdb <- function(server,group,id) {\n"
          + "  cat(\"User: \");\n"
          + "  user <- readline();\n"
          + "  cat(\"Password: \");\n"
          + "  password <- readline();\n"
          + "  url <- paste(server, \"/pentaho/content/cdb/doQuery?\",sep=\"\");\n"
          + "  group <- paste(\"group=\",group,sep=\"\");\n"
          + "  id <- paste(\"&id=\",id,sep=\"\");\n"
          + "  outputType <- \"&outputType=csv\";\n"
          + "  completeUrl <- paste(url, group, id, outputType, \"&userid=\",user,\"&password=\",password,sep=\"\");\n"
          + "  return(read.csv2(completeUrl));\n"
          + "}\n\n";

  @Override
  public String export(String group, String id, String url) {
    String loadString = "cdbData <- readCdb(\"" + url + "\", \"" + group + "\", \"" + id + "\");\n";
    return LOAD_CDB + loadString;
  }

}
