/*
  JBoss, Home of Professional Open Source
  Copyright [2010], Red Hat, Inc., and individual contributors
  by the @authors tag. See the copyright.txt in the distribution for a
  full listing of individual contributors.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package org.jboss.seam.jcr.resolver;

import java.util.Collections;
import java.util.Map;
import java.util.ServiceLoader;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.RepositoryFactory;
import javax.jcr.Session;

import org.jboss.logging.Logger;
import org.jboss.seam.jcr.annotations.JcrConfiguration;
import org.jboss.seam.jcr.events.EventListenerConfig;
import org.jboss.seam.jcr.events.JcrCDIEventListener;
import org.jboss.seam.jcr.repository.SeamEventRepositoryImpl;

/**
 * Resolves Extension Managed {@link Repository} objects
 * 
 * @author george
 * 
 */
public class RepositoryResolver
{
   private final Logger logger = Logger.getLogger(RepositoryResolver.class);

   @Inject
   private BeanManager beanManager;

   @Produces
   public Repository produceRepository(InjectionPoint injectionPoint) throws RepositoryException
   {
      Repository repository = null;
      JcrConfiguration jcrRepo = injectionPoint.getAnnotated().getAnnotation(JcrConfiguration.class);
      if (jcrRepo != null)
      {
         Map<String, String> parameters = Collections.singletonMap(jcrRepo.name(), jcrRepo.value());
         repository = decorateRepository(createPlainRepository(parameters));
      }
      return repository;
   }

   @Produces
   public Session produceSession(InjectionPoint injectionPoint) throws RepositoryException
   {
      Repository repo = produceRepository(injectionPoint);
      Session session = repo.login();
      return decorateSession(session);
   }

   /**
    * JCR 2.0 Default code
    * 
    * @param jcrRepo
    * @return
    * @throws RepositoryException
    */
   private Repository createPlainRepository(Map<String, String> parameters) throws RepositoryException
   {
      Repository repository = null;
      for (RepositoryFactory factory : ServiceLoader.load(RepositoryFactory.class))
      {
         repository = factory.getRepository(parameters);
         if (repository != null)
            break;
      }
      return repository;
   }

   /**
    * Decorates a plain {@link Session} object
    * 
    * @param session Plain {@link Session} instance
    * @return Decorated {@link Session}
    */
   private Session decorateSession(Session session)
   {
      return session;
   }

   /**
    * Decorates a plain {@link Repository} object
    * 
    * @param repository Plain {@link Repository} instance
    * @return Decorated {@link Repository} instance
    */
   private Repository decorateRepository(Repository repository)
   {
      if (repository == null)
      {
         return null;
      }
      JcrCDIEventListener eventListener = new JcrCDIEventListener(beanManager);
      EventListenerConfig config = EventListenerConfig.DEFAULT;
      return new SeamEventRepositoryImpl(repository, config, eventListener);
   }

   public void cleanSession(@Disposes @Any Session session)
   {
      try
      {
         session.save();
         session.logout();
      }
      catch (RepositoryException e)
      {
         logger.error("Error saving session", e);
      }
   }

}
