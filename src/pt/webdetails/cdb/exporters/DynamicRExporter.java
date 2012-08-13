/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.exporters;

import com.github.mustachejava.Mustache;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pdpi
 */
public class DynamicRExporter extends AbstractExporter {

  public DynamicRExporter() {
    this.fileExportExtension = "r";
    this.templateFile = "DynamicR.mustache";
  }

}
