/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pt.webdetails.cda.connections.Connection;
import pt.webdetails.cda.dataaccess.DataAccess;
import pt.webdetails.cda.settings.CdaSettings;
import pt.webdetails.cdb.connector.Connector;
import pt.webdetails.cdb.connector.ConnectorEngine;
import pt.webdetails.cpf.persistence.PersistenceEngine;
        
/**
 *
 * @author pdpi
 */
public class DataAccessEngine {

    public void exportCda(String groupId) {
        PersistenceEngine eng = PersistenceEngine.getInstance();
        try {
            
            JSONObject response = eng.query("select * from queries where groupId = " + groupId);
            JSONArray queries = (JSONArray) response.get("object");
            CdaSettings cda = new CdaSettings(groupId, null);
            for (int i = 0; i < queries.length(); i++) {
                JSONObject query = queries.getJSONObject(i);
                String type = query.getString("type");
                Connector conn = ConnectorEngine.getInstance().getConnector(type);
                
                Connection connection = conn.exportCdaConnection(query);
                cda.addConnection(connection);
                DataAccess dataAccess = conn.exportCdaDataAccess(query);
                cda.addDataAccess(dataAccess);
            }
            //cda.asXML();
        } catch (JSONException e) {
            // TODO: Log this
        }
    }
}
