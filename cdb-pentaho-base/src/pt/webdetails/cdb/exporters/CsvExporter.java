/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2002 - 2026 by Pentaho Canada Inc. : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2030-06-15
 ******************************************************************************/


package pt.webdetails.cdb.exporters;

/**
 * @author pdpi
 */
public class CsvExporter extends AbstractExporter {

  public CsvExporter() {
    this.fileExportExtension = "csv";
  }

  @Override
  public String export( String group, String id, String url ) {
    return ExporterEngine.exportCda( group, id, "csv" );
  }

}
