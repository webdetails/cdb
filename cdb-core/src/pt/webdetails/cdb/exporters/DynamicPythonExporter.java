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
    this.templateFile = "DynamicPython.mustache";
  }

}
