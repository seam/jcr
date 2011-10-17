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
package org.jboss.seam.jcr.test.common.producers;

import javax.inject.Inject;
import javax.jcr.Repository;
import javax.jcr.Session;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.jcr.JcrCDIEventListener;
import org.jboss.seam.jcr.annotations.JcrConfiguration;
import org.jboss.seam.jcr.repository.RepositoryResolverImpl;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test case to verify running injection capabilities against ModeShape's JCR Implementation.
 *
 * @author johnament
 */
@RunWith(Arquillian.class)
public abstract class RepositoryInjectTest {
    
    protected static WebArchive updateArchive(WebArchive archive) {
         return archive
                .addClass(RepositoryInjectTest.class)
                .addPackage(RepositoryResolverImpl.class.getPackage())
                .addPackage(JcrCDIEventListener.class.getPackage())
                .addPackage(JcrConfiguration.class.getPackage());
    }

    @Inject
    private Repository repository;
    
    @Inject
    private Session session;

    @Test
    public void testInjectRepository() throws Exception {
        Assert.assertNotNull(repository);
        Assert.assertNotNull(session);
    }
}
