/*!
* Copyright 2002 - 2014 Webdetails, a Pentaho company.  All rights reserved.
*
* This software was developed by Webdetails and is provided under the terms
* of the Mozilla Public License, Version 2.0, or any later version. You may not use
* this file except in compliance with the license. If you need a copy of the license,
* please go to  http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
*
* Software distributed under the Mozilla Public License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
* the license for the specific language governing your rights and limitations.
*/

package pt.webdetails.cdb.exporters;

import pt.webdetails.cpf.utils.CharsetHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author pdpi
 */
public class StaticRExporter extends AbstractExporter {

  public StaticRExporter() {
    this.fileExportExtension = "zip";
    this.templateFile = "StaticR.mustache";
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
