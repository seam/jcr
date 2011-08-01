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

import java.lang.reflect.Method;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jcr.Node;
import javax.jcr.Session;

import org.jboss.logging.Logger;
import org.jboss.seam.jcr.annotations.JcrConfiguration;
import org.jboss.seam.jcr.annotations.ocm.OCMDao;
import org.jboss.seam.jcr.annotations.ocm.OCMFind;
import org.jboss.seam.jcr.annotations.ocm.OCMQuery;
import org.jboss.seam.jcr.resolver.RepositoryResolver;

public class OCMHandler {
	@Inject public Event<ConvertToObjectEvent> ctoEvent;
	@Inject public RepositoryResolver resolver;
	private Logger logger = Logger.getLogger(OCMHandler.class);
	@AroundInvoke
	public Object handle(InvocationContext ctx) {
		try{
			Class<?> declaringClass = ctx.getMethod().getDeclaringClass();
			Method method = ctx.getMethod();
			Object[] params = ctx.getParameters();
			JcrConfiguration sessionConfig = declaringClass.getAnnotation(OCMDao.class).value();
			Session session = resolver.createSessionFromParameters(sessionConfig, null);
			Class<?> returnType = method.getReturnType();
			if (method.isAnnotationPresent(OCMFind.class)) {
				String uuid = params[0].toString();
				Node foundNode = session.getNodeByIdentifier(uuid);
				Object result = returnType.newInstance();
				ConvertToObjectEvent event = new ConvertToObjectEvent(foundNode,result);
				ctoEvent.fire(event);
				System.out.println("Returning the result "+event);
				return result;
			} else if (method.isAnnotationPresent(OCMQuery.class)) {
				
			}
			
		} catch (Exception e) {
			System.out.println("Unable to handle message");
			e.printStackTrace();
		}
		System.out.println("Returning null");
		return null;
	}
}
