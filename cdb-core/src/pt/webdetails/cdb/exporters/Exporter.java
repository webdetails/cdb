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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author pdpi
 */
public interface Exporter {

  public String export( String group, String id, String url );

  public String export( String group, String id, String url, Map<String, String> params );


  public String getFilename( String group, String id, String url );

  public void binaryExport( String group, String id, String url, OutputStream out ) throws IOException;

  public void binaryExport( String group, String id, String url, Map<String, String> params, OutputStream out )
    throws IOException;

}
