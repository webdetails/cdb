/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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


/**
 *
 * @author pdpi
 */
public class SQLQuery extends AbstractQuery {

  private static final Log logger = LogFactory.getLog(SQLQuery.class);
  private static final String path = "cdb/saiku";

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
