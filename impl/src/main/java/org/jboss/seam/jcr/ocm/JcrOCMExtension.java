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

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.jboss.logging.Logger;
import org.jboss.seam.jcr.annotations.ocm.JcrNode;

/**
 * An extension responsible for finding and binding JcrNodes into metadata to be used for mapping classes to interfaces.
 * 
 * @author johnament
 *
 */
public class JcrOCMExtension implements Extension {
	private Logger logger = Logger.getLogger(JcrOCMExtension.class);
	private OCMMappingStore ocmMappingStore = new OCMMappingStore();
	
	public OCMMappingStore getOCMMappingStore() {
		return ocmMappingStore;
	}
	
	public void registerOCMNodes(@Observes ProcessAnnotatedType<?> pat) {
		JcrNode jcrNode = pat.getAnnotatedType().getAnnotation(JcrNode.class);
		if(jcrNode != null) {
			ocmMappingStore.map(pat.getAnnotatedType(),jcrNode);
			pat.veto();
		} else {
		}
	}
	
}
