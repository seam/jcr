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

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.jcr.JcrCDIEventListener;
import org.jboss.seam.jcr.annotations.JcrConfiguration;
import org.jboss.seam.jcr.repository.RepositoryResolverImpl;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.seam.jcr.ConfigParams.MODESHAPE_URL;
import static org.junit.Assert.assertEquals;

/**
 * Test case for {@link JcrCDIEventListener}
 *
 * @author George Gastaldi
 */
@RunWith(Arquillian.class)
public class JcrCDIEventListenerTest {

    @Inject
    private EventCounterListener counter;

    @Inject
    @JcrConfiguration(name = MODESHAPE_URL, value = "file:target/test-classes/modeshape.xml?repositoryName=CarRepo")
    private Session session;

    @Deployment
    public static JavaArchive createArchive() {
        return ShrinkWrap.create(JavaArchive.class)
        .addPackage(JcrCDIEventListener.class.getPackage())
        .addClass(EventCounterListener.class)
        .addPackage(RepositoryResolverImpl.class.getPackage())
        .addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
    }

    @Test
    public void testOnEventAdded() throws RepositoryException, InterruptedException {
        try {
            // Perform SUT
            Node root = session.getRootNode();
            Node hello = root.addNode("helloWorld");
            hello.setProperty("message", "Hello, World!");
            session.save();
        } finally {
            //logout
            // This is when the observers are fired
            session.logout();
        }
        // ModeShape uses background threads for event firing and similar
        // activities.
        // let's give it 5 seconds to run, then check the bags.
        Thread.sleep(5000);
        // Check that node was added
        assertEquals(1, counter.getCountForType(Event.NODE_ADDED));
        // Properties jcr:primaryType and message added
        // ModeShape adds a few extra properties.
        assertEquals(4, counter.getCountForType(Event.PROPERTY_ADDED));
    }

    @After
    public void cleanUp() throws Exception {
        counter.resetBag();
    }
}
