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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jcr.AccessDeniedException;
import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.version.VersionException;

import org.jboss.logging.Logger;
import org.jboss.seam.jcr.annotations.JcrConfiguration;
import org.jboss.seam.jcr.annotations.ocm.JcrDao;
import org.jboss.seam.jcr.annotations.ocm.JcrFind;
import org.jboss.seam.jcr.annotations.ocm.JcrParam;
import org.jboss.seam.jcr.annotations.ocm.JcrQuery;
import org.jboss.seam.jcr.annotations.ocm.JcrSave;
import org.jboss.seam.jcr.repository.RepositoryResolver;

public class OCMHandler {
	@Inject public Event<ConvertToObject> ctoEvent;
	@Inject public Event<ConvertToNode> ctnEvent;
	@Inject public RepositoryResolver resolver;
	
	private Logger logger = Logger.getLogger(OCMHandler.class);
	@AroundInvoke
	public Object handle(InvocationContext ctx) {
		Session session = null;
		try{
			Class<?> declaringClass = ctx.getMethod().getDeclaringClass();
			Method method = ctx.getMethod();
			Object[] params = ctx.getParameters();
			JcrConfiguration sessionConfig = declaringClass.getAnnotation(JcrDao.class).value();
			session = resolver.createSessionFromParameters(sessionConfig, null);
			Class<?> returnType = method.getReturnType();
			if (method.isAnnotationPresent(JcrFind.class)) {
				String uuid = params[0].toString();
				Node foundNode = session.getNodeByIdentifier(uuid);
				Object result = returnType.newInstance();
				ConvertToObject event = new ConvertToObject(foundNode,result);
				ctoEvent.fire(event);
				logger.debug("Returning the result "+event);
				return result;
			} else if (method.isAnnotationPresent(JcrQuery.class)) {
				JcrQuery jcrQuery = method.getAnnotation(JcrQuery.class);
				String query = jcrQuery.query();
				String language = jcrQuery.language();
				Class<?> target = jcrQuery.resultClass();
				List results = new ArrayList();
				QueryManager queryManager = session.getWorkspace().getQueryManager();
				Query resultQuery = queryManager.createQuery(query,language);
				Annotation[][] annotations = method.getParameterAnnotations();
				for(int i =0;i<params.length;i++) {
					Value value = (Value)params[i];
					JcrParam param = (JcrParam)annotations[i][0];
					resultQuery.bindValue(param.value(), value);
				}
				QueryResult result = resultQuery.execute();
				javax.jcr.NodeIterator nodeIter = result.getNodes();
				while ( nodeIter.hasNext() ) {
				    Node node = nodeIter.nextNode();
				    Object nodeobj = target.newInstance();
				    ConvertToObject event = new ConvertToObject(node,nodeobj);
					ctoEvent.fire(event);
					results.add(nodeobj);
				}
				return results;
			} else if(method.isAnnotationPresent(JcrSave.class)) {
				String path = params[0].toString();
				Object entityToSave = params[1];
				if(session.nodeExists(path)) {
					Node targetNode = session.getNode(path);
					ConvertToNode ctn = new ConvertToNode(entityToSave,targetNode);
					ctnEvent.fire(ctn);
					return targetNode.getIdentifier();
				} else {
					Node parent = session.getRootNode();
					String[] pathLocation = path.replaceFirst("/", "").split("/");
					for(int i =0;i<pathLocation.length;i++) {
						if(parent.hasNode(pathLocation[i])) {
							parent = parent.getNode(pathLocation[i]);
						} else {
							if(i == pathLocation.length) {
								
							} else {
								parent = parent.addNode(pathLocation[i]);
							}
						}
					}
					ConvertToNode ctn = new ConvertToNode(entityToSave,parent);
					ctnEvent.fire(ctn);
					return parent.getIdentifier();
				}
			}
		} catch (Exception e) {
			logger.error("Unable to handle conversion request",e);
		} finally {
			if(session != null) {
				try {
					session.save();
				} catch (RepositoryException e) {
					e.printStackTrace();
				}
				session.logout();
			}
		}
		logger.debug("Returning null from OCMHandler.");
		return null;
	}
}
