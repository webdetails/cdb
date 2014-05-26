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

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import pt.webdetails.cdb.util.AliasedGroup;
import pt.webdetails.cpf.utils.PluginIOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pdpi
 */
public abstract class AbstractExporter implements Exporter {

  protected String fileExportExtension, templateFile;
  private AliasedGroup aliasGroup;

  AbstractExporter() {
    aliasGroup = new AliasedGroup();

    aliasGroup.addClass( this.getClass() );
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
    Map<String, String> values = new HashMap<String, String>();
    values.put( "group", group );
    values.put( "id", id );
    values.put( "url", url );
    Mustache templ;
    try {
      templ = loadTempate( templateFile );
    } catch ( Exception e ) {
      return null;
    }
    StringWriter writer = new StringWriter();
    templ.execute( writer, values );
    return writer.getBuffer().toString();
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

  public Mustache loadTempate( String name ) throws IOException {
    //InputStream templateStream = this.getClass().getResource(name).openStream();
    InputStream templateStream = aliasGroup.getResourceStream( name );
    Reader templateReader = new InputStreamReader( templateStream );
    MustacheFactory mf = new DefaultMustacheFactory();
    return mf.compile( templateReader, name );
  }

}
