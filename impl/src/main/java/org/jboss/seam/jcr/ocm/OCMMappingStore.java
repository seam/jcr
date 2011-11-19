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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.spi.AnnotatedType;

import org.jboss.solder.logging.Logger;
import org.jboss.seam.jcr.annotations.ocm.JcrNode;
import org.jboss.seam.jcr.annotations.ocm.JcrProperty;
import org.jboss.solder.core.Veto;

@Veto
public class OCMMappingStore
{
   private Logger logger = Logger.getLogger(OCMMappingStore.class);
   private Map<Class<?>, OCMMapping> mappingsByClass = new HashMap<Class<?>, OCMMapping>();
   private Map<String, OCMMapping> mappingsByNode = new HashMap<String, OCMMapping>();

   public Map<Class<?>, OCMMapping> getMappingsByClass()
   {
      return mappingsByClass;
   }

   public Map<String, OCMMapping> getMappingsByNode()
   {
      return mappingsByNode;
   }

   void addMapping(OCMMapping mapping)
   {
      mappingsByNode.put(mapping.getNodeType(), mapping);
      mappingsByClass.put(mapping.getNodeClass(), mapping);
   }

   public OCMMapping findMapping(String nodeType)
   {
      return mappingsByNode.get(nodeType);
   }

   public OCMMapping findMapping(Class<?> clazz)
   {
      return mappingsByClass.get(clazz);
   }

   void map(AnnotatedType<?> annotatedType, JcrNode jcrNode)
   {
      OCMMapping mapping = new OCMMapping();
      mapping.setNodeType(jcrNode.value());
      Class<?> clazz = (Class<?>) annotatedType.getBaseType();
      Field[] fields = clazz.getDeclaredFields();
      Method[] methods = clazz.getMethods();
      mapping.setNodeClass(clazz);
      Map<String, JcrProperty> properties = new HashMap<String, JcrProperty>();
      for (Method method : methods)
      {
         if (method.isAnnotationPresent(JcrProperty.class))
         {
            String fieldName = getterToFieldName(method.getName());
            properties.put(fieldName, method.getAnnotation(JcrProperty.class));
         }
      }
      for (Field field : fields)
      {
         logger.debugf("field name: %s", field.getName());
         String fieldName = field.getName();
         String prop = field.getName();
         JcrProperty property = field.getAnnotation(JcrProperty.class);
         if (property != null)
         {
            prop = property.value();
         }
         else if (properties.containsKey(prop))
         {
            prop = properties.get(prop).value();
         }
         logger.debugf("fieldName: %s prop: %s\n", fieldName, prop);
         mapping.getPropertiesToFields().put(prop, field);
         mapping.getFieldsToProperties().put(fieldName, prop);
      }
      addMapping(mapping);
   }

   private static String getterToFieldName(String methodName)
   {
      String fieldName = methodName.replace("get", "");
      return fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
   }
}
