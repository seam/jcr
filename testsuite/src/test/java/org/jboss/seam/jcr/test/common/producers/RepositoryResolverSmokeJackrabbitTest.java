/*
  JBoss, Home of Professional Open Source
  Copyright [2011], Red Hat, Inc., and individual contributors
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
package org.jboss.seam.jcr.test.common.producers;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.jcr.test.common.Utils;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;


/**
 * @author maschmid
 */
@RunWith(Arquillian.class)
public class RepositoryResolverSmokeJackrabbitTest extends RepositoryResolverSmokeTest {
    @Deployment(name="RepositoryResolverSmokeJackrabbit")
    public static WebArchive createArchive() {
        return updateArchive(Utils.baseJackrabbitDeployment()
                .addClass(JackrabbitRepositoryResolverProducer.class)
                .addClass(RepositoryResolverSmokeJackrabbitTest.class));             
    }
}
