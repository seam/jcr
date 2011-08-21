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
package org.jboss.seam.jcr.ocm;

import static org.jboss.seam.jcr.ConfigParams.JACKRABBIT_REPOSITORY_HOME;
import static org.jboss.seam.jcr.ConfigParams.MODESHAPE_URL;

import java.util.List;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.jcr.JcrCDIEventListener;
import org.jboss.seam.jcr.annotations.JcrConfiguration;
import org.jboss.seam.jcr.repository.RepositoryResolverImpl;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class OCMExtensionTest {
	@Inject JcrOCMExtension extension;
	
	@Inject
    @org.jboss.seam.jcr.annotations.JcrConfiguration.List({
          @JcrConfiguration(name = JACKRABBIT_REPOSITORY_HOME, value = "target")
    })
    private Repository repository;
    
    @Inject NodeConverter nodeConverter;
	
    @Deployment
    public static JavaArchive createArchive() {
        return ShrinkWrap.create(JavaArchive.class)
        .addClasses(BasicNode.class,OCMMappingStore.class,OCMMapping.class,NodeConverter.class)
        .addAsServiceProvider(Extension.class, JcrOCMExtension.class)
        .addPackage(RepositoryResolverImpl.class.getPackage())
        .addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
    }
    
    @Test
    public void testRun() {
    	OCMMappingStore ocmMappingStore = extension.getOCMMappingStore();
    	OCMMapping mapping = ocmMappingStore.findMapping(BasicNode.class);
    	Assert.assertNotNull(mapping);
    	Assert.assertEquals(2,mapping.getFieldsToProperties().size());
    	String result = mapping.getFieldsToProperties().get("value");
    	Assert.assertEquals("myvalue", result);
    	String uuid = mapping.getFieldsToProperties().get("uuid");
    	Assert.assertEquals("uuid", uuid);
    }
    
    @Test
    public void testCreateNodeAndOCM() throws Exception {
    	Session session = repository.login(new SimpleCredentials("user", "pass".toCharArray()));
    	try {
            // Perform SUT
            Node root = session.getRootNode();
            Node hello = root.addNode("ocmnode1","nt:unstructured");
            hello.setProperty("myvalue", "Hello, World!");
            
            Node hello2 = root.getNode("ocmnode1");
            BasicNode bn = nodeConverter.nodeToObject(hello2, BasicNode.class);
            Assert.assertEquals("Hello, World!", bn.getValue());
            
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
