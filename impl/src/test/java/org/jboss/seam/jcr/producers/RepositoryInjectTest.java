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

import javax.inject.Inject;
import javax.jcr.Repository;
import javax.jcr.Session;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.jcr.repository.RepositoryResolverImpl;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test case to verify running injection capabilities against ModeShape's JCR Implementation.
 *
 * @author johnament
 */
@RunWith(Arquillian.class)
public class RepositoryInjectTest {
    @Deployment
    public static JavaArchive createArchive() {
        JavaArchive ja = ShrinkWrap.create(JavaArchive.class)
                .addClass(RepositoryResolverProducer.class)
                .addPackage(RepositoryResolverImpl.class.getPackage())
                .addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
        return ja;
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
