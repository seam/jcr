/*
 * JBoss, Home of Professional Open Source
 * Copyright [2010], Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.jcr;

import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

/**
 * Decorates a {@link Repository} object
 * 
 * @author george
 * 
 */
public abstract class AbstractSeamRepositoryImpl implements Repository
{
   private Repository repository;

   public AbstractSeamRepositoryImpl(Repository repository)
   {
      this.repository = repository;
   }

   protected Repository getRepository()
   {
      return repository;
   }

   @Override
   public String[] getDescriptorKeys()
   {
      return repository.getDescriptorKeys();
   }

   public boolean isStandardDescriptor(String key)
   {
      return repository.isStandardDescriptor(key);
   }

   public boolean isSingleValueDescriptor(String key)
   {
      return repository.isSingleValueDescriptor(key);
   }

   public Value getDescriptorValue(String key)
   {
      return repository.getDescriptorValue(key);
   }

   public Value[] getDescriptorValues(String key)
   {
      return repository.getDescriptorValues(key);
   }

   public String getDescriptor(String key)
   {
      return repository.getDescriptor(key);
   }

   public Session login(Credentials credentials, String workspaceName) throws LoginException, NoSuchWorkspaceException, RepositoryException
   {
      return repository.login(credentials, workspaceName);
   }

   public Session login(Credentials credentials) throws LoginException, RepositoryException
   {
      return repository.login(credentials);
   }

   public Session login(String workspaceName) throws LoginException, NoSuchWorkspaceException, RepositoryException
   {
      return repository.login(workspaceName);
   }

   public Session login() throws LoginException, RepositoryException
   {
      return repository.login();
   }
}
