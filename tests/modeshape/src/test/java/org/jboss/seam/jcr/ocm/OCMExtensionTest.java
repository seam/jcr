package org.jboss.seam.jcr.ocm;

import java.util.List;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
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
	
    @Deployment
    public static JavaArchive createArchive() {
        return ShrinkWrap.create(JavaArchive.class)
        .addClass(BasicNode.class).addClass(OCMMappingStore.class).addClass(OCMMapping.class)
        .addAsServiceProvider(Extension.class, JcrOCMExtension.class)
        .addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
    }
    
    @Test
    public void testRun() {
    	OCMMappingStore ocmMappingStore = extension.produceOCMMappingStore();
    	OCMMapping mapping = ocmMappingStore.findMapping(BasicNode.class);
    	Assert.assertNotNull(mapping);
    	Assert.assertEquals(1,mapping.getFieldsToProperties().size());
    	String result = mapping.getFieldsToProperties().get("value");
    	Assert.assertEquals("myvalue", result);
    }
}
