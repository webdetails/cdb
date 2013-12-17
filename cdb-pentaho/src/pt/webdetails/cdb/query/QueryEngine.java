/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */
package pt.webdetails.cdb.query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.pentaho.platform.api.engine.IParameterProvider;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import pt.webdetails.cpf.persistence.PersistenceEngine;
import pt.webdetails.cpf.utils.CharsetHelper;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

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
      PersistenceEngine pe = PersistenceEngine.getInstance();
      // TODO: Another Query exists in CDA, shold change simple name to name and make de correction of sql syntax
      if (!pe.classExists(Query.class.getSimpleName())) {
        pe.initializeClass(Query.class.getSimpleName());
      }
    }
    return _instance;
  }

  public JSONObject listGroups() {
    JSONObject response;
    PersistenceEngine pe = PersistenceEngine.getInstance();
    try {
      Map<String, Object> params = new HashMap<String, Object>();
      //params.put("user", PentahoSessionHolder.getSession().getName());

      // DISBLING MULTI USER SUPPORT BY NOW response = pe.query("select distinct(group) as name, groupName from Query where userid = :user order by groupName", params);
      response = pe.query("select distinct(group) as name, groupName from Query order by groupName", params);
    } catch (JSONException e) {
      return null;
    }
    return response;
  }

  public JSONObject loadGroup(String groupName) {
    JSONObject response;
    PersistenceEngine pe = PersistenceEngine.getInstance();
    try {
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("user", PentahoSessionHolder.getSession().getName());
      params.put("group", groupName);
 
      // DISABLING MULTI USER SUPPORT BY NOW response = pe.query("select * from Query where group = :group and userid = :user order by groupName", params);
      response = pe.query("select * from Query where group = :group order by groupName", params);
      
    } catch (JSONException e) {
      return null;
    }
    return response;
  }

  public JSONObject loadGroup(IParameterProvider requestParams, IParameterProvider pathParams, OutputStream out) {
    String groupName = requestParams.getStringParameter("group", "");
    JSONObject response = loadGroup(groupName);
    try {
      out.write(response.toString(2).getBytes( CharsetHelper.getEncoding() ));
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
        out.write(listGroups().toString(2).getBytes( CharsetHelper.getEncoding() ));
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
