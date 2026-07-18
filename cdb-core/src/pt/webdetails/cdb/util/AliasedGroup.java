/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2002 - 2026 by Pentaho Canada Inc. : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2030-06-15
 ******************************************************************************/


package pt.webdetails.cdb.util;

import pt.webdetails.cdb.CdbEngine;
import pt.webdetails.cpf.repository.api.IReadAccess;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AliasedGroup {
  List<IReadAccess> repositoryList;

  public AliasedGroup() {
    repositoryList = new ArrayList<IReadAccess>();
  }

  public void addClass( Class<?> klass ) {
    repositoryList.add( new PackageResolver( klass ) );
  }

  public void addSolutionDir( String dir ) {
    repositoryList.add( CdbEngine.getEnv().getContentAccessFactory().getPluginRepositoryReader( dir ) );
  }

  public InputStream getResourceStream( String file ) throws FileNotFoundException {
    for ( IReadAccess resolver : repositoryList ) {
      if ( resolver.fileExists( file ) ) {
        try {
          return resolver.getFileInputStream( file );
        } catch ( IOException e ) {
          //carry on
        }
      }
    }
    throw new FileNotFoundException( file );
  }

}
