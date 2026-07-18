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

import pt.webdetails.cpf.InterPluginCall;

import java.util.HashMap;
import java.util.Map;

public class InterPluginBroker {

  public static String exportCda(  String group, String id, String outputType  ) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put( "path", "cdb/queries/" + group + ".cda" );
    params.put( "dataAccessId", id );
    params.put( "outputType", outputType );

    InterPluginCall pluginCall = new InterPluginCall( InterPluginCall.CDA, "doQuery", params );
    return pluginCall.call();
  }
}
