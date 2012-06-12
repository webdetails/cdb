/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.query;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.pentaho.platform.api.engine.IParameterProvider;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import pt.webdetails.cpf.persistence.PersistenceEngine;

/**
 *
 * @author pdpi
 */
public class QueryEngine {

  private static final Log logger = LogFactory.getLog(QueryEngine.class);
  private static QueryEngine _instance;

  public static QueryEngine getInstance() {
    if (_instance == null) {
      _instance = new QueryEngine();
    }
    return _instance;
  }

  public JSONObject listGroups() {
    JSONObject response;
    PersistenceEngine pe = PersistenceEngine.getInstance();
    try {
      Map<String, String> params = new HashMap<String, String>();
      params.put("user", PentahoSessionHolder.getSession().getName());

      response = pe.query("select distinct(group) as name, groupName from Query where userid = :user order by groupName", params);
    } catch (JSONException e) {
      return null;
    }
    return response;
  }

  public JSONObject loadGroup(String groupName) {
    JSONObject response;
    PersistenceEngine pe = PersistenceEngine.getInstance();
    try {
      Map<String, String> params = new HashMap<String, String>();
      params.put("user", PentahoSessionHolder.getSession().getName());
      params.put("group", groupName);
      response = pe.query("select * from Query where group = :group and userid = :user order by groupName", params);
    } catch (JSONException e) {
      return null;
    }
    return response;
  }

  public JSONObject loadGroup(IParameterProvider requestParams, IParameterProvider pathParams, OutputStream out) {
    String groupName = requestParams.getStringParameter("group", "");
    JSONObject response = loadGroup(groupName);
    try {
      out.write(response.toString(2).getBytes("utf-8"));
    } catch (Exception e) {
      logger.error(e);
    }
    return response;
  }

  public JSONObject saveQuery(IParameterProvider requestParams, IParameterProvider pathParams, OutputStream out) {
    return null;
  }

  public JSONObject saveQuery(String data) {
    return null;
  }

  public void process(IParameterProvider requestParams, IParameterProvider pathParams, OutputStream out) {

    String method = requestParams.getStringParameter("method", "");
    if ("listGroups".equals(method)) {
      try {
        out.write(listGroups().toString(2).getBytes("utf-8"));
      } catch (Exception e) {
        logger.error("Error listing queries: " + e);
      }
    } else if ("loadGroup".equals(method)) {
      loadGroup(requestParams, pathParams, out);
    } else {
      logger.error("Unsupported method");
    }
  }

}
