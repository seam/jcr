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
package org.jboss.seam.jcr.events;

import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

import org.jboss.seam.jcr.AbstractSeamRepositoryImpl;

/**
 * Implementation of {@link AbstractSeamRepositoryImpl} that register a CDI enabled
 * {@link EventListener} on {@link Session} objects
 * 
 * @author george
 * 
 */
public class SeamEventRepositoryImpl extends AbstractSeamRepositoryImpl
{
   private JcrCDIEventListener eventListener;
   private EventListenerConfig eventConfig;

   public SeamEventRepositoryImpl(Repository delegated, EventListenerConfig eventConfig, JcrCDIEventListener eventListener)
   {
      super(delegated);
      this.eventListener = eventListener;
      this.eventConfig = eventConfig;
   }

   @Override
   public Session login() throws LoginException, RepositoryException
   {
      Session session = super.login();
      registerListener(session.getWorkspace().getObservationManager());
      return session;
   }

   @Override
   public Session login(Credentials credentials) throws LoginException, RepositoryException
   {
      Session session = super.login(credentials);
      registerListener(session.getWorkspace().getObservationManager());
      return session;
   }

   @Override
   public Session login(String workspaceName) throws LoginException, NoSuchWorkspaceException, RepositoryException
   {
      Session session = super.login(workspaceName);
      registerListener(session.getWorkspace().getObservationManager());
      return session;
   }

   @Override
   public Session login(Credentials credentials, String workspaceName) throws LoginException, NoSuchWorkspaceException, RepositoryException
   {
      Session session = super.login(credentials, workspaceName);
      registerListener(session.getWorkspace().getObservationManager());
      return session;
   }

   /**
    * Registers this {@link EventListener} into an {@link ObservationManager}
    * using the supplied config
    * 
    * @param obsManager - the {@link ObservationManager} object to be
    *           configured.
    * @throws RepositoryException
    */
   private void registerListener(ObservationManager obsManager) throws RepositoryException
   {
      if (eventConfig != null)
      {
         obsManager.addEventListener(eventListener, eventConfig.getEventTypes(), eventConfig.getAbsPath(), eventConfig.isDeep(), eventConfig.getUuid(), eventConfig.getNodeTypeName(), eventConfig.isNoLocal());
      }
   }

}
