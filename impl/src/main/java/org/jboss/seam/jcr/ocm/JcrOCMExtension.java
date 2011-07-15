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
	
	@Produces
	public OCMMappingStore produceOCMMappingStore() {
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
