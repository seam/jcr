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
package org.jboss.seam.jcr.test.common.ocm;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.Session;

import junit.framework.Assert;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.jcr.JcrCDIEventListener;
import org.jboss.seam.jcr.annotations.JcrConfiguration;
import org.jboss.seam.jcr.annotations.ocm.JcrNode;
import org.jboss.seam.jcr.ocm.JcrOCMExtension;
import org.jboss.seam.jcr.ocm.NodeConverter;
import org.jboss.seam.jcr.ocm.OCMMapping;
import org.jboss.seam.jcr.ocm.OCMMappingStore;
import org.jboss.seam.jcr.repository.RepositoryResolverImpl;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public abstract class OCMExtensionTest {
	@Inject JcrOCMExtension extension;
	
    @Inject
    //@JcrConfiguration(name = MODESHAPE_URL, value = "file:target/test-classes/modeshape.xml?repositoryName=CarRepo")
    private Session session;
    
    @Inject NodeConverter nodeConverter;
	
    public static WebArchive updateArchive(WebArchive archive) {
        return archive
            .addClass(OCMExtensionTest.class)
            .addClasses(BasicNode.class,OCMMappingStore.class,OCMMapping.class,NodeConverter.class)            
            .addAsServiceProvider(Extension.class, JcrOCMExtension.class)
            .addPackage(org.jboss.seam.jcr.ocm.JcrOCMExtension.class.getPackage())
            .addPackage(RepositoryResolverImpl.class.getPackage())
            .addPackage(JcrNode.class.getPackage())
            .addPackage(JcrConfiguration.class.getPackage())
            .addPackage(JcrCDIEventListener.class.getPackage());           
    }
    
    @Test
    public void testRun() {
    	OCMMappingStore ocmMappingStore = extension.getOCMMappingStore();
    	OCMMapping mapping = ocmMappingStore.findMapping(BasicNode.class);
    	Assert.assertNotNull(mapping);
    	Assert.assertEquals(3,mapping.getFieldsToProperties().size());
    	String result = mapping.getFieldsToProperties().get("value");
    	Assert.assertEquals("myvalue", result);
    	String uuid = mapping.getFieldsToProperties().get("uuid");
    	Assert.assertEquals("uuid", uuid);
    	String notaproperty = mapping.getFieldsToProperties().get("lordy");
    	Assert.assertEquals("notaproperty", notaproperty);
    }
    
    @Test
    public void testCreateNodeAndOCM() throws Exception {
    	try {
            // Perform SUT
            Node root = session.getRootNode();
            Node hello = root.addNode("ocmnode1","nt:unstructured");
            hello.setProperty("myvalue", "Hello, World!");
            hello.setProperty("notaproperty", "this was saved.");
            
            Node hello2 = root.getNode("ocmnode1");
            BasicNode bn = nodeConverter.nodeToObject(hello2, BasicNode.class);
            Assert.assertEquals("Hello, World!", bn.getValue());
            Assert.assertEquals("this was saved.", bn.getLordy());
            
            Node hello3 = root.addNode("ocmnode3", "nt:unstructured");
            session.save();
            
            BasicNode bn3 = new BasicNode();
            bn3.setValue("some random value");
            Node hello3b = root.getNode("ocmnode3");
            nodeConverter.objectToNode(bn3, hello3b);
            Assert.assertEquals(hello3b.getProperty("myvalue").getString(),bn3.getValue());
        } finally {
            //logout
            // This is when the observers are fired
            session.logout();
        }
    }
}
