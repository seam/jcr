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
package org.jboss.seam.jcr.producers;

import java.util.Collections;
import java.util.Map;
import java.util.ServiceLoader;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.RepositoryFactory;
import javax.jcr.Session;
import org.jboss.logging.Logger;
import org.jboss.seam.jcr.annotations.JcrRepository;

/**
 * Produces {@link Repository} and {@link Session} objects
 * 
 * @author johnament
 */
public class RepositorySessionProducer {

	private final Logger logger = Logger.getLogger(RepositorySessionProducer.class);

	/**
	 * Produces a {@link Repository} based on {@link JcrRepository}
	 * 
	 * TODO: we should add in support SeamManaged.
	 * 
	 * @param ip
	 * @return
	 * @throws RepositoryException
	 */
	@Produces
	@JcrRepository(name="org.apache.jackrabbit.repository.home",value="target")
	public Repository produceJcrRepository(InjectionPoint ip) throws RepositoryException {
		JcrRepository jcrRepo = ip.getAnnotated().getAnnotation(JcrRepository.class);
		return findRepository(jcrRepo);
	}

	/**
	 * Creates a logged in session to the repository. Currently only supports
	 * anonymous access.
	 * 
	 * TODO: we should add in support SeamManaged.
	 * 
	 * @param ip
	 * @return anoymous session
	 * @throws RepositoryException
	 *             if anything not compliant to the JCR implementation occurs
	 */
	@Produces
	@JcrRepository
	public Session produceJcrRepositorySession(InjectionPoint ip) throws RepositoryException {
		JcrRepository jcrRepo = ip.getAnnotated().getAnnotation(JcrRepository.class);
		Repository repo = findRepository(jcrRepo);
		return repo.login();
	}

	private Repository findRepository(JcrRepository jcrRepo) throws RepositoryException {
		Map<String, String> parameters = Collections.singletonMap(jcrRepo.name(), jcrRepo.value());
		Repository repository = null;
		for (RepositoryFactory factory : ServiceLoader.load(RepositoryFactory.class)) {
			repository = factory.getRepository(parameters);
			if (repository != null)
				break;
		}
		return repository;
	}

	public void cleanSession(@Disposes @Any Session session) {
		try {
			session.save();
			session.logout();
		} catch (RepositoryException e) {
			logger.error("Error saving session", e);
		}
	}
}
