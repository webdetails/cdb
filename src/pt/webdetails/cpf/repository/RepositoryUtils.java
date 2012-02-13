/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cpf.repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.platform.api.repository.ISolutionRepository;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;

/**
 *
 * @author pdpi
 */
public class RepositoryUtils {

  public final static String ENCODING = "utf-8";
  private static final Log logger = LogFactory.getLog(RepositoryUtils.class);

  public static void writeSolutionFile(String path, String fileName, String data) {
    try {
      ISolutionRepository solutionRepository = PentahoSystem.get(ISolutionRepository.class, PentahoSessionHolder.getSession());
      solutionRepository.publish(PentahoSystem.getApplicationContext().getSolutionPath(""), path, fileName, data.getBytes(ENCODING), true);
    } catch (Exception e) {
      logger.error(e);
    }
  }

}
