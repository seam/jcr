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
package org.jboss.seam.jcr.test.ocm.dao;


import java.util.List;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.jcr.annotations.JcrConfiguration;
import org.jboss.seam.jcr.annotations.ocm.JcrDao;
import org.jboss.seam.jcr.ocm.ConvertToObject;
import org.jboss.seam.jcr.ocm.JcrOCMExtension;
import org.jboss.seam.jcr.ocm.NodeConverter;
import org.jboss.seam.jcr.ocm.OCMMapping;
import org.jboss.seam.jcr.ocm.OCMMappingStore;
import org.jboss.seam.jcr.producers.RepositoryResolverProducer;
import org.jboss.seam.jcr.repository.RepositoryResolverImpl;
import org.jboss.seam.jcr.test.CredentialProducer;
import org.jboss.seam.jcr.test.Utils;
import org.jboss.seam.jcr.test.ocm.BasicNode;
import org.jboss.seam.solder.serviceHandler.ServiceHandlerExtension;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ServiceTest {
	@Inject
	Instance<BasicNodeDAO> basicDAOInstance;
	
	@Inject NodeConverter nodeConverter;
    
    @Deployment
    public static JavaArchive createArchive() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
        .addClasses(BasicNode.class,OCMMappingStore.class,OCMMapping.class,
            NodeConverter.class,JcrOCMExtension.class,BasicNodeDAO.class, 
            JcrConfiguration.class,RepositoryResolverProducer.class)
        .addAsServiceProvider(Extension.class, JcrOCMExtension.class)
        //.addAsServiceProvider(Extension.class, ServiceHandlerExtension.class)
        .addPackage(RepositoryResolverImpl.class.getPackage())
        .addPackage(JcrDao.class.getPackage())
        .addPackage(ConvertToObject.class.getPackage())
        .addPackage(ServiceHandlerExtension.class.getPackage())
        .addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
        
        if(Utils.isJackrabbit())
            archive.addClass(CredentialProducer.class);
        
        return archive;
    }
    
    @Test
    public void testNothing() throws RepositoryException {
    	BasicNodeDAO basicDAO = basicDAOInstance.get();
    	BasicNode bn = new BasicNode();
    	bn.setValue("this is my node.");
    	String uuid = basicDAO.save("/anypathone",bn);
    	System.out.println("The UUID is: "+uuid);
    	BasicNode bn2 = basicDAO.findBasicNode(uuid);
    	Assert.assertNotNull(bn2);
    	Assert.assertEquals(bn.getValue(), bn2.getValue());
    	List<BasicNode> nodes = basicDAO.findAllNodes();
    	System.out.println(nodes);
    }
    
}
