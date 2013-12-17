/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.exporters;

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
