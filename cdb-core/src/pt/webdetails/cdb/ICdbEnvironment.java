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


package pt.webdetails.cdb;

import pt.webdetails.cdb.bean.factory.ICdbBeanFactory;
import pt.webdetails.cpf.PluginEnvironment;
import pt.webdetails.cpf.exceptions.InitializationException;
import pt.webdetails.cpf.repository.api.IContentAccessFactory;

public interface ICdbEnvironment {

  public void init( ICdbBeanFactory factory ) throws InitializationException;

  public void refresh();

  public IContentAccessFactory getContentAccessFactory();

  PluginEnvironment getPluginEnv();

  public String getPluginRepositoryDir();

  public String getSystemDir();

  public String getPluginId();
}
