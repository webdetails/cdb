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

import pt.webdetails.cpf.utils.CharsetHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author pdpi
 */
public class StaticPythonExporter extends AbstractExporter {

  public StaticPythonExporter() {
    this.fileExportExtension = "zip";
    this.templateFile = "StaticPython.vm";
  }

  @Override
  public void binaryExport( String group, String id, String url, OutputStream out ) throws IOException {
    String src = getSource( group, id, url );
    ZipOutputStream zos = new ZipOutputStream( out );
    zos.putNextEntry( new ZipEntry( id + ".py" ) );
    zos.write( src.getBytes( CharsetHelper.getEncoding() ) );
    zos.putNextEntry( new ZipEntry( id + ".csv" ) );
    zos.write( ExporterEngine.exportCda( group, id, "csv" ).getBytes( CharsetHelper.getEncoding() ) );
    zos.close();
  }

}
