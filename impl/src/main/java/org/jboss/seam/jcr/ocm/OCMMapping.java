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
import java.util.HashMap;
import java.util.Map;

import org.jboss.solder.core.Veto;

@Veto
public class OCMMapping implements java.io.Serializable
{
   private static final long serialVersionUID = 1L;

   private String nodeType;
   private Class<?> nodeClass;

   // private Map<String,String> propertiesToFields = new HashMap<String,String>();
   private Map<String, Field> propertiesToFields = new HashMap<String, Field>();
   private Map<String, String> fieldsToProperties = new HashMap<String, String>();

   public String getNodeType()
   {
      return nodeType;
   }

   public void setNodeType(String nodeType)
   {
      this.nodeType = nodeType;
   }

   public Class<?> getNodeClass()
   {
      return nodeClass;
   }

   public void setNodeClass(Class<?> nodeClass)
   {
      this.nodeClass = nodeClass;
   }

   public Map<String, Field> getPropertiesToFields()
   {
      return propertiesToFields;
   }

   public void setPropertiesToFields(Map<String, Field> propertiesToFields)
   {
      this.propertiesToFields = propertiesToFields;
   }

   public Map<String, String> getFieldsToProperties()
   {
      return fieldsToProperties;
   }

   public void setFieldsToProperties(Map<String, String> fieldsToProperties)
   {
      this.fieldsToProperties = fieldsToProperties;
   }
}
