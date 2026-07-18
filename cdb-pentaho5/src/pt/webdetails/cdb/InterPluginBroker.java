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


package pt.webdetails.cdb;


import org.apache.commons.io.IOUtils;
import pt.webdetails.cpf.InterPluginCall;
import pt.webdetails.cpf.PluginEnvironment;
import pt.webdetails.cpf.plugin.CorePlugin;
import pt.webdetails.cpf.plugincall.api.IPluginCall;
import pt.webdetails.cpf.plugincall.base.CallParameters;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class InterPluginBroker {

  public static void run( Map<String, Object> params, OutputStream out ) throws Exception {
    CallParameters parameters = new CallParameters();
    Iterator<String> it = params.keySet().iterator();
    while ( it.hasNext() ) {
      String key = it.next();
      Object value = params.get( key );
      parameters.put( key, value.toString() );
    }
    IPluginCall pluginCall = PluginEnvironment.env().getPluginCall( CorePlugin.CDE.getId(), "renderer", "render" );
    String returnVal = pluginCall.call( parameters.getParameters() );
    IOUtils.write( returnVal, out );
    out.flush();
  }

  public static String exportCda(  String group, String id, String outputType  ) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put( "path", "public/cdb/queries/" + group + ".cda" );
    params.put( "dataAccessId", id );
    params.put( "outputType", outputType );

    InterPluginCall pluginCall = new InterPluginCall( InterPluginCall.CDA, "doQueryInterPlugin", params );
    return pluginCall.call();
  }

}
