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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

import org.jboss.seam.jcr.events.EventListenerConfig;
import org.jboss.seam.jcr.events.JcrCDIEventListener;

/**
 * {@link InvocationHandler} implementation for injecting a CDI enabled
 * {@link EventListener} on created session objects
 * 
 * @author george
 * 
 */
public class JcrRepositoryInvocationHandler implements InvocationHandler
{
   private JcrCDIEventListener eventListener;
   private EventListenerConfig eventConfig;
   private Repository delegatedRepository;

   public JcrRepositoryInvocationHandler(Repository delegatedRepository, EventListenerConfig eventConfig, JcrCDIEventListener eventListener)
   {
      this.delegatedRepository = delegatedRepository;
      this.eventConfig = eventConfig;
      this.eventListener = eventListener;
   }

   @Override
   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
   {
      Object obj = method.invoke(delegatedRepository, args);
      if (obj instanceof Session)
      {
         Session session = (Session) obj;
         registerListener(session.getWorkspace().getObservationManager());
      }
      return obj;
   }

   /**
    * Registers this {@link EventListener} into an {@link ObservationManager}
    * using the supplied config
    * 
    * @param session - the session to be configured.
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
