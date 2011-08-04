package org.jboss.seam.jcr.repository;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.jboss.seam.jcr.annotations.JcrConfiguration;

public interface RepositoryResolver
{
   public Session createSessionFromParameters(JcrConfiguration configuration,
            JcrConfiguration.List jcrRepoList) throws RepositoryException;
}
