package org.jboss.seam.jcr.ocm;

import javax.jcr.Node;

public class ConvertToNode {
	private Node node;
	private Object object;
	public ConvertToNode(Object object, Node node) {
		this.object = object;
		this.node = node;
	}
	public Node getNode() {
		return node;
	}
	public void setNode(Node node) {
		this.node = node;
	}
	public Object getObject() {
		return object;
	}
}
