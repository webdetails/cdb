/*!
* Copyright 2002 - 2014 Webdetails, a Pentaho company. All rights reserved.
*
* This software was developed by Webdetails and is provided under the terms
* of the Mozilla Public License, Version 2.0, or any later version. You may not use
* this file except in compliance with the license. If you need a copy of the license,
* please go to http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
*
* Software distributed under the Mozilla Public License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. Please refer to
* the license for the specific language governing your rights and limitations.
*/

package pt.webdetails.cdb.api;

import pt.webdetails.cdb.CdbEngine;
import pt.webdetails.cdb.exporters.ExporterEngine;
import pt.webdetails.cdb.util.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.io.IOException;

@Path( "/cdb/api/exporter" )
public class ExportApi {

  @GET
  @Path( "/listExporters" )
  @Produces( "text/javascript" )
  public void listExporters( @Context HttpServletResponse response ) throws IOException {
    CdbEngine.getEnv(); // TODO: FOR REMOVE WHEN FOUND A WAY TO INSTANTIATE IN LIFECYCLE GIVES AN ERROR
    String result = ExporterEngine.getInstance().listExporters();
    JsonUtils.buildJsonResult( response.getOutputStream(), result != null, result );
  }

  @GET
  @Path( "/export" )
  @Produces( "text/javascript" )
  public void export( @QueryParam( MethodParams.EXPORTER ) String exporter,
                      @QueryParam( MethodParams.GROUP ) String group,
                      @QueryParam( MethodParams.ID ) String id,
                      @QueryParam( MethodParams.TO_FILE ) String toFile,
                      @Context HttpServletRequest request, @Context HttpServletResponse response ) throws IOException {
    CdbEngine.getEnv(); // TODO: FOR REMOVE WHEN FOUND A WAY TO INSTANTIATE IN LIFECYCLE GIVES AN ERROR

    if ( toFile.equals( "true" ) ) {
      ExporterEngine.getInstance().exportToFile( request, response, exporter, group, id );
    } else {
      ExporterEngine.getInstance().exportNoFile( request, response, exporter, group, id );
    }
  }


  private class MethodParams {
    public static final String EXPORTER = "exporter";
    public static final String GROUP = "group";
    public static final String ID = "id";
    public static final String TO_FILE = "toFile";
  }
}
