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

import static org.jboss.seam.jcr.ConfigParams.JACKRABBIT_REPOSITORY_HOME;
import static org.junit.Assert.assertEquals;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.observation.Event;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.jcr.JcrCDIEventListener;
import org.jboss.seam.jcr.annotations.JcrConfiguration;
import org.jboss.seam.jcr.annotations.JcrConfiguration.List;
import org.jboss.seam.jcr.repository.RepositoryResolverImpl;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test case for {@link JcrCDIEventListener}
 *
 * @author George Gastaldi
 */
@RunWith(Arquillian.class)
public class JcrCDIEventListenerTest {

    @Inject
    @List({
          @JcrConfiguration(name = JACKRABBIT_REPOSITORY_HOME, value = "target")
    })
    private Repository repository;

    @Inject
    private EventCounterListener counter;

    @Inject
    BeanManager beanManager;

    @Deployment
    public static JavaArchive createArchive() {
        return ShrinkWrap.create(JavaArchive.class).addPackage(JcrCDIEventListener.class.getPackage()).addPackage(RepositoryResolverImpl.class.getPackage()).addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
    }

    /**
     * Jackrabbit only allows save operations on authenticated users.
     * <p/>
     * TODO:Refactor to be injected with credentials
     *
     * @throws Exception
     */
    @Test
    public void testOnEventAdded() throws RepositoryException, InterruptedException {
        Session session = repository.login(new SimpleCredentials("user", "pass".toCharArray()));
        try {
            // Perform SUT
            Node root = session.getRootNode();
            Node hello = root.addNode("helloWorld");
            hello.setProperty("message", "Hello, World!");
            session.save();
        } finally {
            // This is when the observers are fired
            session.logout();
        }
        // let's give it 5 seconds to run, then check the bags.
        Thread.sleep(5000);
        // Check that node was added
        assertEquals(1, counter.getCountForType(Event.NODE_ADDED));
        // Properties jcr:primaryType and message added
        assertEquals(2, counter.getCountForType(Event.PROPERTY_ADDED));
    }
}
