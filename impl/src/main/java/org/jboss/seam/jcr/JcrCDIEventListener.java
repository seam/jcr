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

import java.lang.annotation.Annotation;

import javax.enterprise.inject.spi.BeanManager;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

import org.jboss.solder.logging.Logger;
import org.jboss.seam.jcr.annotations.events.NodeAddedLiteral;
import org.jboss.seam.jcr.annotations.events.NodeMovedLiteral;
import org.jboss.seam.jcr.annotations.events.NodeRemovedLiteral;
import org.jboss.seam.jcr.annotations.events.PersistLiteral;
import org.jboss.seam.jcr.annotations.events.PropertyAddedLiteral;
import org.jboss.seam.jcr.annotations.events.PropertyChangedLiteral;
import org.jboss.seam.jcr.annotations.events.PropertyRemovedLiteral;
import org.jboss.solder.core.Veto;

/**
 * JCR {@link EventListener} for CDI
 * 
 * @author george
 */
@Veto
public final class JcrCDIEventListener implements EventListener
{
   private BeanManager beanManager;
   private Logger logger = Logger.getLogger(JcrCDIEventListener.class);

   public JcrCDIEventListener(BeanManager beanManager)
   {
      this.beanManager = beanManager;
   }

   /**
    * Fired by the JCR spec
    * 
    * @see EventListener#onEvent(EventIterator)
    */
   @Override
   public void onEvent(EventIterator events)
   {
      logger.debugf("Event iterator size: %s", events.getSize());
      while (events.hasNext())
      {
         Event event = events.nextEvent();
         logger.debugf("About to fire an event of type: %s", event.getType());
         beanManager.fireEvent(event, getQualifierByEvent(event));
      }
   }

   /**
    * Returns the qualifier by the event type
    * 
    * @param eventType
    * @return
    */
   Annotation getQualifierByEvent(Event event)
   {
      Annotation qualifier;
      switch (event.getType())
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
         throw new IllegalArgumentException("Event type unrecognized: " + event);
      }
      return qualifier;
   }
}
