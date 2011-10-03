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

import javax.jcr.Credentials;
import javax.enterprise.inject.Instance;
import javax.jcr.Repository;

import org.jboss.seam.jcr.annotations.JcrConfiguration;
import org.jboss.seam.jcr.annotations.events.NodeAddedLiteral;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.jcr.JcrCDIEventListener;
import org.jboss.seam.jcr.repository.RepositoryResolverImpl;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Test case for {@link JcrCDIEventListener}
 *
 * @author George Gastaldi
 */
@RunWith(Arquillian.class)
public abstract class JcrCDIEventListenerTest {

    @Inject
    private EventCounterListener counter;

    @Inject
    //@JcrConfiguration(name = MODESHAPE_URL, value = "file:target/test-classes/modeshape.xml?repositoryName=CarRepo")
    private Repository repository;
    
    @Inject
    Instance<Credentials> credInst;
    
    public static WebArchive updateArchive(WebArchive archive) {
        return archive
            .addClasses(JcrCDIEventListenerTest.class)
            .addPackage(JcrCDIEventListener.class.getPackage())
            .addClasses(EventCounterListener.class)
            .addPackage(RepositoryResolverImpl.class.getPackage())        
            .addPackage(JcrConfiguration.class.getPackage())
            .addPackage(NodeAddedLiteral.class.getPackage())
            .addAsLibraries(
                DependencyResolvers.use(MavenDependencyResolver.class)
                .loadReposFromPom("pom.xml")
                .artifact("commons-collections:commons-collections")                  
                .resolveAs(JavaArchive.class));
    }
    
    protected boolean isJackrabbit() {
        return false;
    }

    @Test
    public void testOnEventAdded() throws RepositoryException, InterruptedException {
        Session session = null;
        if(this.credInst.isUnsatisfied()) {
            session = repository.login();
        } else {
            session = repository.login(credInst.get());
        }
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
        if(isJackrabbit()) {
            assertEquals(2, counter.getCountForType(Event.PROPERTY_ADDED));
        } else {
            assertEquals(4, counter.getCountForType(Event.PROPERTY_ADDED));
        }
    }

    @After
    public void cleanUp() throws Exception {
        counter.resetBag();
    }
}
