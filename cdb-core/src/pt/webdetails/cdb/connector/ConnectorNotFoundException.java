/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.connector;

import pt.webdetails.cdb.exporters.*;

/**
 *
 * @author pdpi
 */
public class ConnectorNotFoundException extends ConnectorException {

  public ConnectorNotFoundException() {
  }

  public ConnectorNotFoundException(Exception cause) {
    super(cause);
  }

}
