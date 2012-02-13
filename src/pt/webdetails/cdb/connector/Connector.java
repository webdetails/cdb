/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.connector;

import org.json.JSONObject;
import pt.webdetails.cdb.query.Query;
import pt.webdetails.cda.dataaccess.DataAccess;
import pt.webdetails.cda.connections.Connection;

/**
 *
 * @author pdpi
 */
public interface Connector {

    public Query newQuery();

    void editQuery();

    void viewQuery();

    void deleteQuery();

    DataAccess exportCdaDataAccess(JSONObject query);

    Connection exportCdaConnection(JSONObject query);
}
