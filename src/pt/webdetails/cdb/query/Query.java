/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */
package pt.webdetails.cdb.query;

import org.json.JSONObject;
import pt.webdetails.cdb.connector.Connector;

/**
 *
 * @author pdpi
 */
public interface Query extends Connector {

  /*Import/Export API*/
  public JSONObject toJSON();

  public void fromJSON(JSONObject json);

  /* Getters/Setters */
  public String getName();

  public String getGroup();

  public String getId();

  public String getKey();

  public Object getProperty(String prop);

  /*DB persistence */
  public void store();

  public void load(String key);

  public void reload();

}
