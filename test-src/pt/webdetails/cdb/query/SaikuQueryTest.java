/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */
package pt.webdetails.cdb.query;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.util.Assert;
import pt.webdetails.cda.connections.Connection;
import pt.webdetails.cda.dataaccess.DataAccess;

/**
 *
 * @author pedrovale
 */
public class SaikuQueryTest {
  
  @Test
  public void testSaikuQueryExportCda() throws JSONException {
    SaikuQuery sq = new SaikuQuery();
    
    JSONObject queryDefinition = new JSONObject("{name: 'Name', '@rid': '4:5',group: 'group1', guid :'id', definition: {jndi: 'j', cube: 'c', catalog: 'ca', query: 'q'}}");    
    sq.fromJSON(queryDefinition);
    
    Connection c = sq.exportCdaConnection();    
    Assert.isTrue(c instanceof pt.webdetails.cda.connections.mondrian.JndiConnection);
    
    DataAccess da = sq.exportCdaDataAccess();        
    Assert.isTrue(da instanceof pt.webdetails.cda.dataaccess.MdxDataAccess);
    
  }
  
}
