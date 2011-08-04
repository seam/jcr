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
package org.jboss.seam.jcr;

import java.io.Serializable;

import javax.jcr.observation.Event;

/**
 * JCR Event listener configuration
 * 
 * @author george
 */
public final class EventListenerConfig implements Serializable
{
   private static final long serialVersionUID = 1L;

   public static final int ALL_EVENTS = Event.NODE_ADDED | Event.NODE_MOVED | Event.NODE_REMOVED | Event.PERSIST
            | Event.PROPERTY_ADDED | Event.PROPERTY_CHANGED | Event.PROPERTY_REMOVED;

   private String absPath;
   private boolean deep;
   private int eventTypes;
   private boolean noLocal;
   private String[] nodeTypeName;
   private String[] uuid;

   /**
    * Default config, if none is specified
    */
   public static final EventListenerConfig DEFAULT = new EventListenerConfig("/", true, ALL_EVENTS, false, null, null);

   public EventListenerConfig(String absPath, boolean deep, int eventTypes, boolean noLocal, String[] nodeTypeName,
            String[] uuid)
   {
      super();
      this.absPath = absPath;
      this.deep = deep;
      this.eventTypes = eventTypes;
      this.noLocal = noLocal;
      this.nodeTypeName = nodeTypeName;
      this.uuid = uuid;
   }

   public String getAbsPath()
   {
      return absPath;
   }

   public boolean isDeep()
   {
      return deep;
   }

   public int getEventTypes()
   {
      return eventTypes;
   }

   public boolean isNoLocal()
   {
      return noLocal;
   }

   public String[] getNodeTypeName()
   {
      return nodeTypeName;
   }

   public String[] getUuid()
   {
      return uuid;
   }

}
