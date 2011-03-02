/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jboss.seam.jcr.producers;

import javax.inject.Inject;
import javax.jcr.Repository;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.jcr.annotations.JcrRepository;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author johnament
 */
@RunWith(Arquillian.class)
public class RepositoryInjectTest {
    @Deployment
    public static Archive<?> createDeployment()
   {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addClasses(RepositorySessionProducer.class,JcrRepository.class)
                .addManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        return archive;
    }

    @Inject @JcrRepository(name="org.modeshape.jcr.URL",value="file:modeshape.xml?repositoryName=CarRepo")
    Repository carRepo;

    @Test
    public void testInjectRepository() {

    }
}
