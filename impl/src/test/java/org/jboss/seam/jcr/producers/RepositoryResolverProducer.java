package org.jboss.seam.jcr.producers;

import static org.jboss.seam.jcr.ConfigParams.JACKRABBIT_REPOSITORY_HOME;
import static org.jboss.seam.jcr.ConfigParams.MODESHAPE_URL;

import java.util.Collections;
import java.util.Map;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.jboss.seam.jcr.ConfigParams;
import org.jboss.seam.jcr.test.Utils;
import org.jboss.seam.solder.core.ExtensionManaged;

public class RepositoryResolverProducer {

    @Produces
    @ExtensionManaged
    @Named(ConfigParams.JCR_REPOSITORY_CONFIG_MAP)
    public Map<String, String> producesDefaultConfig() {
        if(Utils.isJackrabbit()) {
            return Collections.singletonMap(JACKRABBIT_REPOSITORY_HOME, "target");
        } else if(Utils.isModeshape()) {
            return Collections.singletonMap(MODESHAPE_URL, "file:target/test-classes/modeshape.xml?repositoryName=CarRepo");
        }
        throw new RuntimeException("Container misconfiguration.  No JCR Implementation found.");
    }
}
