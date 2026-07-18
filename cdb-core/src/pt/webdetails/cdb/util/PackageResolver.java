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
    return 0L; //File#lastModified default
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
