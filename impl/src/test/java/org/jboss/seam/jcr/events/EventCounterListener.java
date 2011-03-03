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

import javax.enterprise.event.Observes;
import javax.inject.Singleton;
import javax.jcr.observation.Event;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.bag.HashBag;
import org.jboss.seam.jcr.annotations.events.NodeAdded;
import org.jboss.seam.jcr.annotations.events.NodeMoved;
import org.jboss.seam.jcr.annotations.events.NodeRemoved;
import org.jboss.seam.jcr.annotations.events.Persist;
import org.jboss.seam.jcr.annotations.events.PropertyAdded;
import org.jboss.seam.jcr.annotations.events.PropertyChanged;
import org.jboss.seam.jcr.annotations.events.PropertyRemoved;

/**
 * Used on test cases
 * 
 * @author George Gastaldi
 * 
 */
@Singleton
public class EventCounterListener
{
   private Bag counter = new HashBag();

   public void onNodeAdded(@Observes @NodeAdded Event event)
   {
      counter.add(event.getType());
   }

   public void onNodeRemoved(@Observes @NodeRemoved Event event)
   {
      counter.add(event.getType());
   }

   public void onNodeMoved(@Observes @NodeMoved Event event)
   {
      counter.add(event.getType());
   }

   public void onPersist(@Observes @Persist Event event)
   {
      counter.add(event.getType());
   }

   public void onPropertyAdded(@Observes @PropertyAdded Event event)
   {
      counter.add(event.getType());
   }

   public void onPropertyRemoved(@Observes @PropertyRemoved Event event)
   {
      counter.add(event.getType());
   }

   public void onPropertyChanged(@Observes @PropertyChanged Event event)
   {
      counter.add(event.getType());
   }

   public int getCountForType(int type)
   {
      return counter.getCount(type);
   }
}