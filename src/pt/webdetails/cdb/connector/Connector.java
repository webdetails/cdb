/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.connector;

import org.json.JSONObject;
import pt.webdetails.cda.dataaccess.DataAccess;
import pt.webdetails.cda.connections.Connection;

/**
 *
 * @author pdpi
 */
public interface Connector {

  public DataAccess exportCdaDataAccess();

  public Connection exportCdaConnection();

  public void copy(String newGuid);

  public void delete();
}
