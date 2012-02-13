/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.connector;

import org.json.JSONException;
import org.json.JSONObject;
import pt.webdetails.cda.connections.Connection;
import pt.webdetails.cda.connections.mondrian.JndiConnection;
import pt.webdetails.cda.connections.mondrian.MondrianJndiConnectionInfo;
import pt.webdetails.cda.dataaccess.DataAccess;
import pt.webdetails.cda.dataaccess.MdxDataAccess;

/**
 *
 * @author pdpi
 */
public class SaikuConnector implements Connector {

    @Override
    public DataAccess exportCdaDataAccess(JSONObject query) {
        String id, name, queryContent;
        try {
            id = query.getString("name");
            JSONObject definition = new JSONObject(query.getString("definition"));
            name = id;
            queryContent = definition.getString("query");
            DataAccess dataAccess = new MdxDataAccess(id,name,id,queryContent);
            //dataAccess;
            return dataAccess;
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public Connection exportCdaConnection(JSONObject query) {
        String jndi, catalog, cube, id;
        try {
            id = query.getString("name");
            JSONObject definition = new JSONObject(query.getString("definition"));
            jndi = definition.getString("jndi");
            cube = definition.getString("cube");
            catalog = definition.getString("catalog");
            MondrianJndiConnectionInfo cinfo = new MondrianJndiConnectionInfo(jndi, catalog, cube);
            Connection conn = new JndiConnection(id, cinfo);
            return conn;
        } catch (JSONException e) {
            return null;
        }
    }
}
