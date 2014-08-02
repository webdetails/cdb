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

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import pt.webdetails.cdb.util.AliasedGroup;
import pt.webdetails.cdb.util.CdbEnvironment;
import pt.webdetails.cpf.repository.api.IReadAccess;
import pt.webdetails.cpf.utils.PluginIOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author pdpi
 */
public abstract class AbstractExporter implements Exporter {

  private static final String EXPORTERS_DIR = "resources/";
  protected String fileExportExtension, templateFile;

  AbstractExporter() {
  }

  @Override
  public String export( String group, String id, String url, Map<String, String> params ) {
    throw new UnsupportedOperationException( "Not implemented yet" );
  }

  @Override
  public String export( String group, String id, String url ) {
    return getSource( group, id, url );
  }

  protected String getSource( String group, String id, String url ) {

    VelocityEngine ve = new VelocityEngine();
    ve.init();

    VelocityContext context = new VelocityContext();
    context.put( "group", group );
    context.put( "id", id );
    context.put( "url", url );

    Reader templateReader;

    try {
      IReadAccess read = CdbEnvironment.getPluginSystemReader();
      InputStream templateStream = read.getFileInputStream( EXPORTERS_DIR + templateFile );
      templateReader = new InputStreamReader( templateStream );

    } catch ( Exception e ) {
      return null;

    }

    StringWriter writer = new StringWriter();
    ve.evaluate( context, writer, templateFile, templateReader );

    return writer.toString();


  }

  @Override
  public void binaryExport( String group, String id, String url, Map<String, String> params, OutputStream out ) {
    throw new UnsupportedOperationException( "Not implemented yet" );
  }

  @Override
  public void binaryExport( String group, String id, String url, OutputStream out ) throws IOException {
    PluginIOUtils.writeOutAndFlush( out, export( group, id, url ) );
  }

  @Override
  public String getFilename( String group, String id, String url ) {
    return group + "-" + id + "." + fileExportExtension;
  }

}
