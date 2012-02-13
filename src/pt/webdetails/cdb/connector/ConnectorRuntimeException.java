/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.connector;

/**
 *
 * @author pdpi
 */
public class ConnectorRuntimeException extends ConnectorException {

  public ConnectorRuntimeException() {
  }

  public ConnectorRuntimeException(Exception cause) {
    super(cause);
  }

}
