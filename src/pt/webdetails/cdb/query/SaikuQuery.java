/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */
package pt.webdetails.cdb.query;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import pt.webdetails.cda.connections.Connection;
import pt.webdetails.cda.connections.mondrian.MondrianJndiConnectionInfo;
import pt.webdetails.cda.connections.mondrian.JndiConnection;
import pt.webdetails.cda.dataaccess.DataAccess;
import pt.webdetails.cda.dataaccess.MdxDataAccess;
import pt.webdetails.cpf.Util;
import pt.webdetails.cpf.repository.RepositoryAccess;

/**
 *
 * @author pdpi
 */
public class SaikuQuery extends AbstractQuery {

  private static final Log logger = LogFactory.getLog(SaikuQuery.class);
  private static final String path = "cdb/saiku";

  @Override
  public DataAccess exportCdaDataAccess() {
    String id, name, queryContent;
    id = getName();
    name = id;
    queryContent = getProperty("query").toString();
    DataAccess dataAccess = new MdxDataAccess(id, name, id, queryContent);
    //dataAccess;
    return dataAccess;
  }

  @Override
  public Connection exportCdaConnection() {
    String jndi, cube, catalog;
    jndi = getProperty("jndi").toString();
    cube = getProperty("cube").toString();
    catalog = getProperty("catalog").toString();

    MondrianJndiConnectionInfo cinfo = new MondrianJndiConnectionInfo(jndi, catalog, cube);
    Connection conn = new JndiConnection(getName(), cinfo);
    return conn;
  }

  @Override
  public void copy(String newGuid) {
    String newFileName = newGuid + ".saiku",
            oldFileName = getId() + ".saiku";
    try {
      RepositoryAccess.getRepository().copySolutionFile(Util.joinPath(path, oldFileName), Util.joinPath(path, newFileName));
    } catch (IOException e) {
      logger.error(e);
    }
  }

  @Override
  public void delete() {
    String fileName = getId() + ".saiku";
    RepositoryAccess.getRepository().removeFile(path + "/" + fileName);
  }

  @Override
  public JSONObject toJSON() {
    JSONObject json = super.toJSON();
    return json;
  }

  @Override
  public void fromJSON(JSONObject json) {
    super.fromJSON(json);
  }

}
