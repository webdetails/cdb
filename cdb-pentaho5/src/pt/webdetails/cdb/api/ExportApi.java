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
