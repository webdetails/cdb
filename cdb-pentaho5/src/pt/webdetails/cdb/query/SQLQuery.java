/*!
* Copyright 2002 - 2013 Webdetails, a Pentaho company. All rights reserved.
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
package pt.webdetails.cdb.query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import pt.webdetails.cda.connections.Connection;
import pt.webdetails.cda.connections.sql.JndiConnection;
import pt.webdetails.cda.connections.sql.SqlJndiConnectionInfo;
import pt.webdetails.cda.dataaccess.DataAccess;
import pt.webdetails.cda.dataaccess.SqlDataAccess;

public class SQLQuery extends AbstractQuery {

  private static final Log logger = LogFactory.getLog(SQLQuery.class);
  //private static final String path = "cdb/queries";

  @Override
  public DataAccess exportCdaDataAccess() {
    String id, name, queryContent;
    id = getName();
    name = id;
    queryContent = getProperty("query").toString();
    DataAccess dataAccess = new SqlDataAccess(id, name, id, queryContent);
    //dataAccess;
    return dataAccess;
  }

  @Override
  public Connection exportCdaConnection() {
    String jndi;
    jndi = getProperty("jndi").toString();
    SqlJndiConnectionInfo cinfo = new SqlJndiConnectionInfo(jndi, null, null, null, null);
    Connection conn = new JndiConnection(getName(), cinfo);
    return conn;
  }

  @Override
  public void copy(String newGuid) {
    /* No-op */
  }

  @Override
  public void delete() {
    /* No-op */
  }

  @Override
  public JSONObject toJSON() {
    JSONObject json = super.toJSON();
    return json;
  }

  @Override
  public void fromJSON(JSONObject json) {
    super.fromJSON(json);
  }

}
