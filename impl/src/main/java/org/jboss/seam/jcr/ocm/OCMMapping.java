package org.jboss.seam.jcr.ocm;

import java.util.HashMap;
import java.util.Map;

import org.jboss.seam.solder.core.Veto;

@Veto
public class OCMMapping implements java.io.Serializable {
	private String nodeType;
	private Class<?> nodeClass;
	private Map<String,String> propertiesToFields = new HashMap<String,String>();
	private Map<String,String> fieldsToProperties = new HashMap<String,String>();
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public Class<?> getNodeClass() {
		return nodeClass;
	}
	public void setNodeClass(Class<?> nodeClass) {
		this.nodeClass = nodeClass;
	}
	public Map<String, String> getPropertiesToFields() {
		return propertiesToFields;
	}
	public void setPropertiesToFields(Map<String, String> propertiesToFields) {
		this.propertiesToFields = propertiesToFields;
	}
	public Map<String, String> getFieldsToProperties() {
		return fieldsToProperties;
	}
	public void setFieldsToProperties(Map<String, String> fieldsToProperties) {
		this.fieldsToProperties = fieldsToProperties;
	}
}
