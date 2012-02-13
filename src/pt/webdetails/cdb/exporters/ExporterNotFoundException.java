/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb;

import pt.webdetails.cdb.exporters.*;

/**
 *
 * @author pdpi
 */
public class ExporterNotFoundException extends Exception {

  public ExporterNotFoundException() {
  }

  public ExporterNotFoundException(Exception cause) {
    super(cause);
  }

}
