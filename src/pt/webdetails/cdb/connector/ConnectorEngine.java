/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.connector;

import java.io.OutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pentaho.platform.api.engine.IParameterProvider;
import pt.webdetails.cda.connections.Connection;
import pt.webdetails.cda.dataaccess.DataAccess;
import pt.webdetails.cda.settings.CdaSettings;
import pt.webdetails.cpf.persistence.PersistenceEngine;
import pt.webdetails.cpf.repository.RepositoryUtils;

/**
 *
 * @author pdpi
 */
public class ConnectorEngine {

  static ConnectorEngine instance;
  private static final Log logger = LogFactory.getLog(ConnectorEngine.class);

  private ConnectorEngine() {
  }

  public static ConnectorEngine getInstance() {
    if (instance == null) {
      instance = new ConnectorEngine();
    }
    return instance;
  }

  protected Connector getConnector(String connectorName) throws ConnectorRuntimeException, ConnectorNotFoundException {
    try {
      Class connectorClass = Class.forName("pt.webdetails.cdb.connector." + connectorName + "Connector");
      return (Connector) connectorClass.getConstructor().newInstance();
    } catch (ClassNotFoundException e) {
      throw new ConnectorNotFoundException(e);
    } catch (Exception e) {
      throw new ConnectorRuntimeException(e);
    }
  }

  public String[] listConnectors() {
    String[] types = {"Saiku"};
    return types;
  }

  public static void exportCda(String groupId) {
    PersistenceEngine eng = PersistenceEngine.getInstance();
    try {

      JSONObject response = eng.query("select * from Query where group = \"" + groupId + "\"");
      JSONArray queries = (JSONArray) response.get("object");
      CdaSettings cda = new CdaSettings(groupId, null);
      for (int i = 0; i < queries.length(); i++) {
        JSONObject query = queries.getJSONObject(i);
        String type = query.getString("type");
        try {
          Connector conn = ConnectorEngine.getInstance().getConnector(type);

          Connection connection = conn.exportCdaConnection(query);
          cda.addConnection(connection);
          DataAccess dataAccess = conn.exportCdaDataAccess(query);
          cda.addDataAccess(dataAccess);
        } catch (ConnectorException e) {
          logger.error(e);
        }
      }
      RepositoryUtils.writeSolutionFile("cdb/queries", groupId + ".cda", cda.asXML());
    } catch (Exception e) {
      logger.error(e);
    }
  }

  public void process(IParameterProvider requestParams, IParameterProvider pathParams, OutputStream out) {

    String method = requestParams.getStringParameter("method", "");
    if ("exportCda".equals(method)) {
      String group = requestParams.getStringParameter("group", "");
      exportCda(group);
    } else {
      logger.error("Unsupported method");
    }
  }

}
