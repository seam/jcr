package org.jboss.seam.jcr.ocm;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.spi.AnnotatedType;

import org.jboss.logging.Logger;
import org.jboss.seam.jcr.annotations.ocm.JcrNode;
import org.jboss.seam.jcr.annotations.ocm.JcrProperty;
import org.jboss.seam.solder.core.Veto;

@Veto
public class OCMMappingStore {
	private Logger logger = Logger.getLogger(OCMMappingStore.class);
	private Map<Class<?>,OCMMapping> mappingsByClass = new HashMap<Class<?>,OCMMapping>();
	private Map<String,OCMMapping> mappingsByNode = new HashMap<String,OCMMapping>();
	public Map<Class<?>, OCMMapping> getMappingsByClass() {
		return mappingsByClass;
	}
	public Map<String, OCMMapping> getMappingsByNode() {
		return mappingsByNode;
	}
	void addMapping(OCMMapping mapping) {
		mappingsByNode.put(mapping.getNodeType(), mapping);
		mappingsByClass.put(mapping.getNodeClass(), mapping);
	}
	public OCMMapping findMapping(String nodeType) {
		return mappingsByNode.get(nodeType);
	}
	public OCMMapping findMapping(Class<?> clazz) {
		return mappingsByClass.get(clazz);
	}
	void map(AnnotatedType<?> annotatedType, JcrNode jcrNode) {
		OCMMapping mapping = new OCMMapping();
		mapping.setNodeType(jcrNode.value());
		Class<?> clazz = (Class<?>)annotatedType.getBaseType();
		Field[] fields = clazz.getDeclaredFields();
		mapping.setNodeClass(clazz);
		for(Field field : fields) {
			System.out.println(field);
			System.out.println("field name: "+field.getName());
			String fieldName = field.getName();
			String prop = field.getName();
			JcrProperty property = field.getAnnotation(JcrProperty.class);
			if(property != null) {
				prop = property.value();
			}
			System.out.printf("fieldName: %s prop: %s\n", fieldName,prop);
			mapping.getPropertiesToFields().put(prop,fieldName);
			mapping.getFieldsToProperties().put(fieldName,prop);
		}
		addMapping(mapping);
	}
}
