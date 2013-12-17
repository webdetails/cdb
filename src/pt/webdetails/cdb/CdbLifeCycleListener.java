/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */

package pt.webdetails.cdb;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.platform.api.engine.PluginLifecycleException;
import pt.webdetails.cpf.PluginEnvironment;
import pt.webdetails.cpf.SimpleLifeCycleListener;

public class CdbLifeCycleListener extends SimpleLifeCycleListener {

    static Log logger = LogFactory.getLog( CdbLifeCycleListener.class );

    @Override
    public void init() throws PluginLifecycleException {
        logger.debug("Init for CDB");
        getEnvironment();
    }

    @Override
    public void loaded() throws PluginLifecycleException {
        super.loaded();
    }

    @Override
    public void unLoaded() throws PluginLifecycleException {
        logger.debug( "Unload for CDB" );
    }

    @Override
    public PluginEnvironment getEnvironment() {
        return (PluginEnvironment) CdbEngine.getInstance().getEnvironment();
    }

}
