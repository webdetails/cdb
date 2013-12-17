/*!
* Copyright 2002 - 2013 Webdetails, a Pentaho company. All rights reserved.
*
* This software was developed by Webdetails and is provided under the terms
* of the Mozilla Public License, Version 2.0, or any later version. You may not use
* this file except in compliance with the license. If you need a copy of the license,
* please go to http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
*
* Software distributed under the Mozilla Public License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. Please refer to
* the license for the specific language governing your rights and limitations.
*/
package pt.webdetails.cdb.query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.pentaho.platform.api.engine.IParameterProvider;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import pt.webdetails.cpf.persistence.PersistenceEngine;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

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
      response = pe.query("select distinct(group) as name, group from Query order by group", params);
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
      response = pe.query("select * from Query where group = :group order by group", params);
      
    } catch (JSONException e) {
      return null;
    }
    return response;
  }

  public JSONObject loadGroup(IParameterProvider requestParams, IParameterProvider pathParams) {
    String groupName = requestParams.getStringParameter("group", "");
    JSONObject response = loadGroup(groupName);

    return response;
  }

  public JSONObject saveQuery(IParameterProvider requestParams, IParameterProvider pathParams, OutputStream out) {
    return null;
  }

  public JSONObject saveQuery(String data) {
    return null;
  }
}
