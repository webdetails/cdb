/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.connector;

/**
 *
 * @author pdpi
 */
public class ConnectorEngine {

    static ConnectorEngine instance;

    private ConnectorEngine() {
    }

    public static ConnectorEngine getInstance() {
        if (instance == null) {
            instance = new ConnectorEngine();
        }
        return instance;
    }

    public Connector getConnector(String type) {
        return null;
    }

    public String newDataAccess() {
        return "";
    }

    public String editDataAccess() {
        return "";
    }

    public String viewDataAccess() {
        return "";
    }

    public String deleteDataAccess() {
        return "";
    }
}
