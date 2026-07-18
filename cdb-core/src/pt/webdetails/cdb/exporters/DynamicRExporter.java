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
public class DynamicRExporter extends AbstractExporter {

  public DynamicRExporter() {
    this.fileExportExtension = "r";
    this.templateFile = "DynamicR.vm";
  }

}
