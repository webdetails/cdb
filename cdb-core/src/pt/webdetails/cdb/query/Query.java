/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2002 - 2026 by Pentaho Canada Inc. : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2030-06-15
 ******************************************************************************/


package pt.webdetails.cdb.query;

import org.json.JSONObject;
import pt.webdetails.cdb.connector.Connector;

/**
 * @author pdpi
 */
public interface Query extends Connector {

  /*Import/Export API*/
  public JSONObject toJSON();

  public void fromJSON( JSONObject json );

  /* Getters/Setters */
  public String getName();

  public String getGroup();

  public String getId();

  public String getKey();

  public Object getProperty( String prop );

  /*DB persistence */
  public void store();

  public void load( String key );

  public void reload();

}
