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
package pt.webdetails.cdb.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.NotImplementedException;
import pt.webdetails.cpf.repository.api.IBasicFile;
import pt.webdetails.cpf.repository.api.IBasicFileFilter;
import pt.webdetails.cpf.repository.api.IReadAccess;
import pt.webdetails.cpf.repository.util.RepositoryHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class PackageResolver implements IReadAccess {

  protected final String basePath;
  protected final ClassLoader classLoader;

  public PackageResolver( Class<?> classe ) {
    this.classLoader = classe.getClassLoader();
    this.basePath = classe.getPackage().getName().replaceAll( "\\.", "/" );
  }

  @Override
  public InputStream getFileInputStream( String path ) throws IOException {
    path = FilenameUtils.normalize( RepositoryHelper.appendPath( basePath, path ) );
    URL url = RepositoryHelper.getClosestResource( classLoader, path );
    if ( url != null ) {
      return url.openStream();
    } else {
      return null;
    }
  }

  @Override
  public boolean fileExists( String path ) {
    path = FilenameUtils.normalize( RepositoryHelper.appendPath( basePath, path ) );
    return RepositoryHelper.getClosestResource( classLoader, path ) != null;
  }

  @Override
  public long getLastModified( String path ) {
    URL url = RepositoryHelper.getClosestResource( classLoader, path );
    if ( url != null ) {
      File file = new File( url.getPath() );
      return file.lastModified();
    }
    return 0L;//File#lastModified default
  }

  @Override
  public List<IBasicFile> listFiles( String s, IBasicFileFilter iBasicFileFilter, int i, boolean b, boolean b2 ) {
    throw new NotImplementedException();
  }

  @Override
  public List<IBasicFile> listFiles( String path, IBasicFileFilter filter, int maxDepth, boolean includeDirs ) {
    throw new NotImplementedException();
  }

  @Override
  @Deprecated
  public List<IBasicFile> listFiles( String path, IBasicFileFilter filter, int maxDepth ) {
    throw new NotImplementedException();
  }

  @Override
  public List<IBasicFile> listFiles( String path, IBasicFileFilter filter ) {
    throw new NotImplementedException();
  }

  @Override
  public IBasicFile fetchFile( String path ) {
    throw new NotImplementedException();
  }
}
