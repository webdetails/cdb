/*!
* Copyright 2002 - 2013 Webdetails, a Pentaho company. All rights reserved.
*
* This software was developed by Webdetails and is provided under the terms
* of the Mozilla Public License, Version 2.0, or any later version. You may not use
* this file except in compliance with the license. If you need a copy of the license,
* please go to http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
*
* Software distributed under the Mozilla Public License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. Please refer to
* the license for the specific language governing your rights and limitations.
*/
package pt.webdetails.cdb;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pt.webdetails.cdb.bean.factory.CoreBeanFactory;
import pt.webdetails.cdb.bean.factory.ICdbBeanFactory;
import pt.webdetails.cpf.Util;
import pt.webdetails.cpf.exceptions.InitializationException;
import pt.webdetails.cpf.repository.api.IRWAccess;



public class CdbEngine {

  private static CdbEngine instance;
  protected static Log logger = LogFactory.getLog( CdbEngine.class );
  private ICdbEnvironment cdbEnv;

  private CdbEngine() {
    logger.debug( "Starting ElementEngine" );
  }

  /*private CdbEngine(ICdbEnvironment environment) {
    this();
    this.cdbEnv = environment;
  } */

  public static CdbEngine getInstance() {

    if ( instance == null ) {
      instance = new CdbEngine();
      try {
        initialize();
      } catch ( Exception ex ) {
        logger.fatal( "Error initializing CdbEngine: " + Util.getExceptionDescription( ex ) );
      }
    }

    return instance;
  }

  public ICdbEnvironment getEnvironment() {
    return getInstance().cdbEnv;
  }

  private static void initialize() throws InitializationException {
    if ( instance.cdbEnv == null ) {

      ICdbBeanFactory factory = new CoreBeanFactory();

      // try to get the environment from the configuration
      // will return the DefaultCdaEnvironment by default
      ICdbEnvironment env = instance.getConfiguredEnvironment( factory );

      if ( env != null ) {
        env.init( factory );
      }

      instance.cdbEnv = env;
      // XXX figure out where to put ensureBasicDirs
      instance.ensureBasicDirs();
      //PersistenceEngine.getInstance();



    }
  }

  private void ensureBasicDirs() {
    IRWAccess repoBase = getEnv().getContentAccessFactory().getPluginRepositoryWriter(null);
    // TODO: better error messages
    if ( !ensureDirExists( repoBase, CdbConstants.SolutionFolders.SAIKU) ) {
      logger.error( "Couldn't find or create CDB saiku dir." );
    }
    if ( !ensureDirExists( repoBase, CdbConstants.SolutionFolders.QUERIES ) ) {
      logger.error( "Couldn't find or create CDB queries dir." );
    }
  }

  private boolean ensureDirExists( IRWAccess access, String relPath ) {
    return access.fileExists( relPath ) || access.createFolder( relPath );
  }

  public static ICdbEnvironment getEnv() {
    return getInstance().getEnvironment();
  }

  protected synchronized ICdbEnvironment getConfiguredEnvironment( ICdbBeanFactory factory )
    throws InitializationException {

    Object obj = factory.getBean( ICdbEnvironment.class.getSimpleName() );

    if ( obj != null && obj instanceof ICdbEnvironment) {
      return (ICdbEnvironment) obj;
    } else {
      String msg = "No bean found for ICdbEnvironment!!";
      logger.fatal( msg );
      throw new InitializationException( msg, null );
    }
  }


}
