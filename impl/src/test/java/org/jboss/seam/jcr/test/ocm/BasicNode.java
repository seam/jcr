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
package org.jboss.seam.jcr.test.ocm;

import org.jboss.seam.jcr.annotations.ocm.JcrNode;
import org.jboss.seam.jcr.annotations.ocm.JcrProperty;

@JcrNode("nt:unstructured")
public class BasicNode implements java.io.Serializable {
	@JcrProperty("myvalue")
	private String value;
	private String uuid;
	private String lordy;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@JcrProperty("notaproperty")
	public String getLordy() {
		return lordy;
	}

	public void setLordy(String lordy) {
		this.lordy = lordy;
	}
	
}
