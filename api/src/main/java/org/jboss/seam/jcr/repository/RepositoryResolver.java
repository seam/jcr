package org.jboss.seam.jcr.repository;

import java.util.Map;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.jboss.seam.jcr.annotations.JcrConfiguration;

public interface RepositoryResolver
{
   public Session createSessionFromParameters(JcrConfiguration configuration,
            JcrConfiguration.List jcrRepoList) throws RepositoryException;
   
   public Session createSessionFromParameters(JcrConfiguration configuration,
            JcrConfiguration.List jcrRepoList, Map<String,String> defaults) 
           throws RepositoryException;
}
