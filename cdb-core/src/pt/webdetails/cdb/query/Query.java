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
