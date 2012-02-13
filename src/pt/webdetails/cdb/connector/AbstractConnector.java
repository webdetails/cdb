/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.connector;

import pt.webdetails.cdb.query.Query;

/**
 *
 * @author pdpi
 */
public abstract class AbstractConnector implements Connector {

    @Override
    public Query newQuery() {
        return null;
    }
    
    @Override
    public void editQuery() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void viewQuery() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteQuery() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
