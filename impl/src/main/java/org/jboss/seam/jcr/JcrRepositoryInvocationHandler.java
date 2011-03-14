package org.jboss.seam.jcr;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

import org.jboss.seam.jcr.events.EventListenerConfig;
import org.jboss.seam.jcr.events.JcrCDIEventListener;

public class JcrRepositoryInvocationHandler implements InvocationHandler
{
   private JcrCDIEventListener eventListener;
   private EventListenerConfig eventConfig;
   private Repository delegatedRepository;

   public JcrRepositoryInvocationHandler(Repository delegatedRepository, EventListenerConfig eventConfig, JcrCDIEventListener eventListener)
   {
      this.delegatedRepository = delegatedRepository;
      this.eventConfig = eventConfig;
      this.eventListener = eventListener;
   }

   @Override
   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
   {
      Object obj = method.invoke(delegatedRepository, args);
      if (obj instanceof Session)
      {
         Session session = (Session) obj;
         registerListener(session);
      }
      return obj;
   }

   /**
    * Registers this {@link EventListener} into an {@link ObservationManager}
    * using the supplied config
    * 
    * @param ip - the injection point.
    * @param beanManager - the CDI bean manager.
    * @param session - the session to be configured.
    * @throws RepositoryException
    */
   private void registerListener(Session session) throws RepositoryException
   {
      if (eventConfig != null)
      {
         session.getWorkspace().getObservationManager().addEventListener(eventListener, eventConfig.getEventTypes(), eventConfig.getAbsPath(), eventConfig.isDeep(), eventConfig.getUuid(), eventConfig.getNodeTypeName(), eventConfig.isNoLocal());
      }
   }
}
