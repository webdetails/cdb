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
