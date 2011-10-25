/*
  JBoss, Home of Professional Open Source
  Copyright [2010], Red Hat, Inc., and individual contributors
  by the @authors tag. See the copyright.txt in the distribution for a
  full listing of individual contributors.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package org.jboss.seam.jcr.test.common;

import java.util.ServiceLoader;
import javax.jcr.RepositoryFactory;

import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 *
 * @author jament
 */
public class Utils {
    
    private static BaseDeploymentFactory getBaseDeploymentFactory() {
        if ("weld-ee-embedded-1.1".equals(System.getProperty("arquillian"))) {
            return new EmbeddedBaseDeploymentFactory();
        }
        else {
            return new DefaultBaseDeploymentFactory();
        }
    }
    
    public static WebArchive baseModeshapeDeployment() {      
        return getBaseDeploymentFactory().baseModeshapeDeployment();
    }
    
    public static WebArchive baseJackrabbitDeployment() {        
        return getBaseDeploymentFactory().baseJackrabbitDeployment();
    }
    
    public static RepositoryFactory locateRepositoryFactory() {
        ServiceLoader<RepositoryFactory> rfsl = ServiceLoader.load(RepositoryFactory.class);
        return rfsl.iterator().next();
    }
    
    public static boolean isModeshape() {
        RepositoryFactory rf = locateRepositoryFactory();
        return rf.getClass().getCanonicalName().startsWith("org.modeshape");
    }
    
    public static boolean isJackrabbit() {
        RepositoryFactory rf = locateRepositoryFactory();
        return rf.getClass().getCanonicalName().startsWith("org.apache.jackrabbit");
    }
}
