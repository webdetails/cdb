/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.connector;

/**
 *
 * @author pdpi
 */
public abstract class ConnectorException extends Exception {

  private static final long serialVersionUID = 1L;

  public ConnectorException() {
    super();
  }

  public ConnectorException(Exception cause) {
    super(cause);
  }

}
