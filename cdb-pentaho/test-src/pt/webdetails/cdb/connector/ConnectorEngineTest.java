/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */
package pt.webdetails.cdb.connector;

import junit.framework.Assert;
import org.junit.Test;


/**
 *
 * @author pedrovale
 */

public class ConnectorEngineTest {
  
  
  @Test
  public void testGetConnector() throws ConnectorRuntimeException, ConnectorNotFoundException {
    ConnectorEngine ce = ConnectorEngine.getInstance();
    
    Assert.assertNotNull( ce.getConnector("Saiku") );
  }


  
  
  
}
