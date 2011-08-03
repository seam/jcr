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

import javax.jcr.Node;

public class ConvertToObject {
	public ConvertToObject(Node node, Object object) {
		this.node = node;
		this.object = object;
		this.clazz = object.getClass();
	}
	private Node node;
	private Class<?> clazz;
	private Object object;
	public Node getNode() {
		return node;
	}
	public Class<?> getClazz() {
		return clazz;
	}
	public Object getObject() {
		return object;
	}
}
