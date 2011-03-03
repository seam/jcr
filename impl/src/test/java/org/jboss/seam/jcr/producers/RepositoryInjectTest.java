/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jboss.seam.jcr.producers;

import java.io.File;
import java.net.URL;
import javax.inject.Inject;
import javax.jcr.Repository;
import javax.jcr.Session;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.jcr.annotations.JcrRepository;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author johnament
 */
@RunWith(Arquillian.class)
public class RepositoryInjectTest {
    @Deployment
   public static JavaArchive createArchive()
   {
      return ShrinkWrap.create(JavaArchive.class).addPackage(RepositorySessionProducer.class.getPackage()).addManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
   }
   ///src/git/seam/jcr/impl/target/test-classes/
    @Inject @JcrRepository(name="org.modeshape.jcr.URL",value="file:target/test-classes/modeshape.xml?repositoryName=CarRepo")
    Repository carRepo;

    @Inject @JcrRepository(name="org.modeshape.jcr.URL",value="file:target/test-classes/modeshape.xml?repositoryName=CarRepo")
    Session carSession;

    @Test
    public void testInjectRepository() throws Exception {
        Assert.assertNotNull(carRepo);
        Assert.assertNotNull(carSession);
    }
}
