/*!
* Copyright 2002 - 2014 Webdetails, a Pentaho company.  All rights reserved.
*
* This software was developed by Webdetails and is provided under the terms
* of the Mozilla Public License, Version 2.0, or any later version. You may not use
* this file except in compliance with the license. If you need a copy of the license,
* please go to  http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
*
* Software distributed under the Mozilla Public License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
* the license for the specific language governing your rights and limitations.
*/

package pt.webdetails.cdb.query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pt.webdetails.cpf.persistence.PersistenceEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pdpi
 */
public abstract class AbstractQuery implements Query {

  private String id, group, name, key;
  private Map<String, Object> properties;
  protected static final Log logger = LogFactory.getLog( pt.webdetails.cdb.query.AbstractQuery.class );

  protected AbstractQuery() {
    properties = new HashMap<String, Object>();
  }

  public String getId() {
    return id;
  }

  protected void setId( String id ) {
    this.id = id;
  }

  @Override
  public String getGroup() {
    return group;
  }

  protected void setGroup( String group ) {
    this.group = group;
  }

  @Override
  public String getName() {
    return name;
  }

  protected void setName( String name ) {
    this.name = name;
  }

  @Override
  public String getKey() {
    return key;
  }

  protected void setKey( String key ) {
    this.key = key;
  }

  @Override
  public Object getProperty( String prop ) {
    return properties.get( prop );
  }

  @Override
  public JSONObject toJSON() {
    try {
      JSONObject json = new JSONObject();
      json.put( "id", id );
      json.put( "key", key );
      json.put( "group", group );
      json.put( "name", name );
      json.put( "definition", properties );
      return json;
    } catch ( JSONException jse ) {
      return null;
    }
  }

  @Override
  public void fromJSON( JSONObject json ) {
    String _id, _key, _group, _name;
    HashMap<String, Object> _properties = new HashMap<String, Object>();
    try {
      /* Load everything into temporary variables */
      _id = json.getString( "guid" );
      _key = json.getString( "@rid" );
      _group = json.getString( "group" );
      _name = json.getString( "name" );
      JSONObject props = json.getJSONObject( "definition" );
      for ( String key : JSONObject.getNames( props ) ) {
        _properties.put( key, props.get( key ) );
      }
      /* Seems like we managed to safely load everything, so it's
       * now safe to copy all the values over to the object
       */
      id = _id;
      key = _key;
      group = _group;
      name = _name;
      properties = _properties;
    } catch ( JSONException jse ) {
      logger.error( "Error while reading values from JSON", jse );
    }
  }

  @Override
  public void store() {
    throw new UnsupportedOperationException( "Not supported yet." );
  }

  @Override
  public void reload() {
    load( getKey() );
  }

  @Override
  public void load( String key ) {
    PersistenceEngine eng = PersistenceEngine.getInstance();
    try {

      Map<String, Object> params = new HashMap<String, Object>();
      params.put( "id", id );
      JSONObject response = eng.query( "select * from Query where @rid = :id", params );
      JSONObject query = (JSONObject) ( (JSONArray) response.get( "object" ) ).get( 0 );
      fromJSON( query );
    } catch ( Exception e ) {
      logger.error( e );
    }
  }

}
