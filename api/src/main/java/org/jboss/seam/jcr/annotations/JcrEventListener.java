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
package org.jboss.seam.jcr.annotations;

import javax.jcr.observation.Event;

/**
 * Represents that an event listener will be bound with this repository
 * 
 * @author george
 * 
 */
public @interface JcrEventListener
{
   public static final int ALL_EVENTS = Event.NODE_ADDED | Event.NODE_MOVED | Event.NODE_REMOVED | Event.PERSIST | Event.PROPERTY_ADDED | Event.PROPERTY_CHANGED | Event.PROPERTY_REMOVED;

   String absPath();

   boolean deep();

   int eventTypes() default ALL_EVENTS;

   boolean noLocal();

   String[] nodeTypeName() default {};

   String[] uuid() default {};

}
