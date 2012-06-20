/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */

package pt.webdetails.cdb.exporters;

/**
 *
 * @author pdpi
 */
public class CsvExporter extends AbstractExporter {

  public CsvExporter() {
    this.fileExportExtension = "csv";
  }
  @Override
  public String export(String group, String id, String url) {
    return ExporterEngine.exportCda(group, id, "csv");
  }

}
