/*!
* Copyright 2002 - 2014 Webdetails, a Pentaho company.  All rights reserved.
*
* This software was developed by Webdetails and is provided under the terms
* of the Mozilla Public License, Version 2.0, or any later version. You may not use
* this file except in compliance with the license. If you need a copy of the license,
* please go to  http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
*
* Software distributed under the Mozilla Public License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
* the license for the specific language governing your rights and limitations.
*/

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
