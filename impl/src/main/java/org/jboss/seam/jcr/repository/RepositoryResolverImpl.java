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
package org.jboss.seam.jcr.repository;

import static org.jboss.seam.solder.reflection.AnnotationInspector.getAnnotation;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.RepositoryFactory;
import javax.jcr.Session;

import org.jboss.seam.logging.Logger;
import org.jboss.seam.solder.util.service.ServiceLoader;
import org.jboss.seam.jcr.ConfigParams;
import org.jboss.seam.jcr.EventListenerConfig;
import org.jboss.seam.jcr.JcrCDIEventListener;
import org.jboss.seam.jcr.annotations.JcrConfiguration;

/**
 * Resolves Extension Managed {@link Repository} objects
 * 
 * @author george
 */
public class RepositoryResolverImpl implements RepositoryResolver
{
   private final Logger logger = Logger.getLogger(RepositoryResolverImpl.class);

   @Inject
   private BeanManager beanManager;

   @Inject
   @Named(ConfigParams.JCR_REPOSITORY_CONFIG_MAP)
   private Instance<Map<String, String>> configParameters;

   @Inject
   private Instance<Credentials> credentialsInstance;

   @Inject
   private @Named(ConfigParams.WORKSPACE_NAME)
   Instance<String> workspaceInstance;

   /**
    * Produces a repository based on the injection point.
    * 
    * If no Map was
    * 
    * @param injectionPoint
    * @return
    * @throws RepositoryException
    */
   @Produces
   public Repository produceRepository(final InjectionPoint injectionPoint) throws RepositoryException
   {
      Annotation[] qualifiers = getQualifiers(injectionPoint);
      Map<String, String> parameters;
      Instance<Map<String, String>> qualifiedParams = configParameters.select(qualifiers);
      if (qualifiedParams.isUnsatisfied())
      {
         JcrConfiguration jcrRepo = getAnnotation(injectionPoint.getAnnotated(), JcrConfiguration.class, beanManager);
         JcrConfiguration.List jcrRepoList = getAnnotation(injectionPoint.getAnnotated(), JcrConfiguration.List.class,
                  beanManager);
         parameters = buildParameters(jcrRepo, jcrRepoList);
      }
      else
      {
         parameters = qualifiedParams.get();
      }
      return decorateRepository(createPlainRepository(parameters));
   }

   public Map<String, String> buildParameters(JcrConfiguration configuration,
            JcrConfiguration.List jcrRepoList)
   {
      Map<String, String> parameters = new HashMap<String, String>();
      if (configuration != null && configuration.name() != "")
      {
         parameters.put(configuration.name(), configuration.value());
      }
      if (jcrRepoList != null)
      {
         for (JcrConfiguration conf : jcrRepoList.value())
         {
            parameters.put(conf.name(), conf.value());
         }
      }
      return parameters;
   }

   public Session createSessionFromParameters(JcrConfiguration configuration,
            JcrConfiguration.List jcrRepoList) throws RepositoryException
   {
      Map<String, String> parameters = buildParameters(configuration, jcrRepoList);
      Repository repository = decorateRepository(createPlainRepository(parameters));
      Credentials c = credentialsInstance.isUnsatisfied() ? null : credentialsInstance.get();
      String workspaceName = workspaceInstance.isUnsatisfied() ? null : workspaceInstance.get();
      return repository.login(c, workspaceName);
   }
   
   public Session createSessionFromParameters(JcrConfiguration configuration,
            JcrConfiguration.List jcrRepoList, Map<String,String> defaults) throws RepositoryException
   {
      Map<String, String> parameters = new HashMap<String,String>();
      if(defaults != null)
        parameters.putAll(defaults);
      parameters.putAll(buildParameters(configuration, jcrRepoList));
      Repository repository = decorateRepository(createPlainRepository(parameters));
      Credentials c = credentialsInstance.isUnsatisfied() ? null : credentialsInstance.get();
      String workspaceName = workspaceInstance.isUnsatisfied() ? null : workspaceInstance.get();
      return repository.login(c, workspaceName);
   }

   @Produces
   public Session produceSession(InjectionPoint injectionPoint) throws RepositoryException
   {
      Repository repo = produceRepository(injectionPoint);
      Annotation[] qualifiers = getQualifiers(injectionPoint);
      Credentials c = credentialsInstance.isUnsatisfied() ? null : credentialsInstance.select(qualifiers).get();
      String workspaceName = workspaceInstance.isUnsatisfied() ? null : workspaceInstance.select(qualifiers).get();
      return repo.login(c, workspaceName);
   }

   private Annotation[] getQualifiers(InjectionPoint injectionPoint)
   {
      Annotation[] qualifiers = injectionPoint.getQualifiers().toArray(new Annotation[0]);
      return qualifiers;
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
