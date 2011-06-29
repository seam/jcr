package org.jboss.seam.jcr.producers;

import static org.jboss.seam.jcr.ConfigParams.JACKRABBIT_REPOSITORY_HOME;

import java.util.Collections;
import java.util.Map;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.jboss.seam.jcr.ConfigParams;
import org.jboss.seam.solder.core.ExtensionManaged;

public class RepositoryResolverProducer {

    @Produces
    @ExtensionManaged
    @Named(ConfigParams.JCR_REPOSITORY_CONFIG_MAP)
    public Map<String, String> producesDefaultConfig() {
        return Collections.singletonMap(JACKRABBIT_REPOSITORY_HOME, "target");
    }
}
