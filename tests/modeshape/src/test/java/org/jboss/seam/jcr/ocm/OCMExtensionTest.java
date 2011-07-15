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

import static org.jboss.seam.jcr.ConfigParams.MODESHAPE_URL;

import java.util.List;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.Session;

import junit.framework.Assert;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.jcr.annotations.JcrConfiguration;
import org.jboss.seam.jcr.events.JcrCDIEventListener;
import org.jboss.seam.jcr.resolver.RepositoryResolver;
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
    @JcrConfiguration(name = MODESHAPE_URL, value = "file:target/test-classes/modeshape.xml?repositoryName=CarRepo")
    private Session session;
    
    @Inject NodeConverter nodeConverter;
	
    @Deployment
    public static JavaArchive createArchive() {
        return ShrinkWrap.create(JavaArchive.class)
        .addClasses(BasicNode.class,OCMMappingStore.class,OCMMapping.class,NodeConverter.class)
        .addAsServiceProvider(Extension.class, JcrOCMExtension.class)
        .addPackage(RepositoryResolver.class.getPackage())
        .addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
    }
    
    @Test
    public void testRun() {
    	OCMMappingStore ocmMappingStore = extension.produceOCMMappingStore();
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
    	try {
            // Perform SUT
            Node root = session.getRootNode();
            Node hello = root.addNode("ocmnode1","nt:unstructured");
            hello.setProperty("myvalue", "Hello, World!");
            
            Node hello2 = root.getNode("ocmnode1");
            BasicNode bn = nodeConverter.nodeToObject(hello2, BasicNode.class);
            Assert.assertEquals("Hello, World!", bn.getValue());
            session.save();
        } finally {
            //logout
            // This is when the observers are fired
            session.logout();
        }
    }
}
