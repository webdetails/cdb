/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.connector;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pentaho.platform.api.engine.IParameterProvider;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import pt.webdetails.cda.connections.Connection;
import pt.webdetails.cda.dataaccess.DataAccess;
import pt.webdetails.cda.settings.CdaSettings;
import pt.webdetails.cdb.query.Query;
import pt.webdetails.cpf.persistence.PersistenceEngine;
import pt.webdetails.cpf.repository.RepositoryAccess;

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
      Class<?> connectorClass = Class.forName("pt.webdetails.cdb.connector." + connectorName + "Connector");
      return (Connector) connectorClass.getConstructor().newInstance();
    } catch (ClassNotFoundException e) {
      throw new ConnectorNotFoundException(e);
    } catch (Exception e) {
      throw new ConnectorRuntimeException(e);
    }
  }

  protected Query getQuery(String key) throws ConnectorRuntimeException, ConnectorNotFoundException {
    PersistenceEngine eng = PersistenceEngine.getInstance();
    try {
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("id", key);
      JSONObject response = eng.query("select * from Query where @rid = :id", params);
      JSONObject query = (JSONObject) ((JSONArray) response.get("object")).get(0);
      return getQuery(query);
    } catch (JSONException e) {
      throw new ConnectorRuntimeException(e);
    }
  }

  protected Query getQuery(JSONObject json) throws ConnectorRuntimeException, ConnectorNotFoundException {
    try {
      String type = json.getString("type");
      Class<?> queryClass = Class.forName("pt.webdetails.cdb.query." + type + "Query");
      Query q = (Query) queryClass.getConstructor().newInstance();
      q.fromJSON(json);
      return q;
    } catch (ClassNotFoundException e) {
      throw new ConnectorNotFoundException(e);
    } catch (Exception e) {
      throw new ConnectorRuntimeException(e);
    }
  }

  public String[] listConnectors() {
    String[] types = {"Saiku", "SQL"};
    return types;
  }

  public static void exportCda(String groupId) {
    PersistenceEngine eng = PersistenceEngine.getInstance();
    try {

      Map<String, Object> params = new HashMap<String, Object>();
      params.put("group", groupId);
      params.put("user", PentahoSessionHolder.getSession().getName());


      // DISABLING MULTI USER SUPPORT BY NOW JSONObject response = eng.query("select * from Query where group = :group and userid = :user",params);
      JSONObject response = eng.query("select * from Query where group = :group ", params);


      JSONArray queries = (JSONArray) response.get("object");
      CdaSettings cda = new CdaSettings(groupId, null);
      for (int i = 0; i < queries.length(); i++) {
        JSONObject query = queries.getJSONObject(i);
        String type = query.getString("type");
        Query q = ConnectorEngine.getInstance().getQuery(query);
        try {
          Connector conn = ConnectorEngine.getInstance().getConnector(type);

          Connection connection = q.exportCdaConnection();
          if (connection == null) {
            logger.error("Connection is null. Something is wrong with query "
                    + query.getString("name") + ". Ignoring.");
            continue;
          }
          cda.addConnection(connection);
          DataAccess dataAccess = q.exportCdaDataAccess();
          cda.addDataAccess(dataAccess);
        } catch (ConnectorException e) {
          logger.error(e);
        }
      }
      String fileName = PentahoSessionHolder.getSession().getName() + "." + groupId + ".cda";
      RepositoryAccess repository = RepositoryAccess.getRepository();
      repository.publishFile("cdb/queries/" + fileName, cda.asXML(), true);

      //RepositoryUtils.writeSolutionFile("cdb/queries", PentahoSessionHolder.getSession().getName() + "." + groupId + ".cda", cda.asXML());
    } catch (Exception e) {
      logger.error("Exception while exporting CDA.", e);
    }
  }

  public void process(IParameterProvider requestParams, IParameterProvider pathParams, OutputStream out) {

    String method = requestParams.getStringParameter("method", "");
    if ("exportCda".equals(method)) {
      String group = requestParams.getStringParameter("group", "");
      exportCda(group);
    } else if ("copyQuery".equals(method)) {
      String id = requestParams.getStringParameter("id", ""),
              newGuid = requestParams.getStringParameter("newguid", "");
      copyQuery(id, newGuid);
    } else if ("deleteQuery".equals(method)) {
      String id = requestParams.getStringParameter("id", "");
      deleteQuery(id);
    } else {
      logger.error("Unsupported method");
    }
  }

  public void copyQuery(String id, String newGuid) {
    PersistenceEngine eng = PersistenceEngine.getInstance();
    try {
      getQuery(id).copy(newGuid);
    } catch (Exception e) {
      logger.error(e);
    }
  }

  public void deleteQuery(String id) {
    PersistenceEngine eng = PersistenceEngine.getInstance();
    try {
      getQuery(id).delete();
    } catch (Exception e) {
      logger.error(e);
    }
  }

}
