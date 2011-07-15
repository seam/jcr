package org.jboss.seam.jcr.ocm;

import org.jboss.seam.jcr.annotations.ocm.JcrNode;
import org.jboss.seam.jcr.annotations.ocm.JcrProperty;

@JcrNode("nt:unstructured")
public class BasicNode implements java.io.Serializable {
	@JcrProperty("myvalue")
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
