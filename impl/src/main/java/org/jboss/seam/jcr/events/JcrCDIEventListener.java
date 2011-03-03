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
package org.jboss.seam.jcr.events;

import java.lang.annotation.Annotation;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

/**
 * JCR {@link EventListener} for CDI
 * 
 * @author george
 * 
 */
@Singleton
public final class JcrCDIEventListener implements EventListener
{

   /**
    * Producer of CDI Events
    */
   @Inject
   private javax.enterprise.event.Event<javax.jcr.observation.Event> eventManager;

   /**
    * Fired by the JCR spec
    */
   @Override
   public void onEvent(EventIterator events)
   {
      while (events.hasNext())
      {
         Event event = events.nextEvent();
         Annotation qualifier = getQualifierByEventType(event.getType());
         eventManager.select(qualifier).fire(event);
      }
   }

   /**
    * Returns the qualifier by the event type
    * 
    * @param eventType
    * @return
    */
   Annotation getQualifierByEventType(int eventType)
   {
      Annotation qualifier;
      switch (eventType)
      {
      case Event.NODE_ADDED:
         qualifier = NodeAddedLiteral.INSTANCE;
         break;
      case Event.NODE_MOVED:
         qualifier = NodeMovedLiteral.INSTANCE;
         break;
      case Event.NODE_REMOVED:
         qualifier = NodeRemovedLiteral.INSTANCE;
         break;
      case Event.PERSIST:
         qualifier = PersistLiteral.INSTANCE;
         break;
      case Event.PROPERTY_ADDED:
         qualifier = PropertyAddedLiteral.INSTANCE;
         break;
      case Event.PROPERTY_CHANGED:
         qualifier = PropertyChangedLiteral.INSTANCE;
         break;
      case Event.PROPERTY_REMOVED:
         qualifier = PropertyRemovedLiteral.INSTANCE;
         break;
      default:
         throw new IllegalArgumentException("Event type unrecognized: " + eventType);
      }
      return qualifier;
   }
}
