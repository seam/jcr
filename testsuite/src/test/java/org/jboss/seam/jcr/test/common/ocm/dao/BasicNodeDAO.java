/*
  JBoss, Home of Professional Open Source
  Copyright [2010], Red Hat, Inc., and individual contributors
  by the @authors tag. See the copyright.txt in the distribution for a
  full listing of individual contributors.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package org.jboss.seam.jcr.test.common.ocm.dao;

import java.util.List;

import org.jboss.seam.jcr.annotations.ocm.JcrDao;
import org.jboss.seam.jcr.annotations.ocm.JcrFind;
import org.jboss.seam.jcr.annotations.ocm.JcrQuery;
import org.jboss.seam.jcr.annotations.ocm.JcrSave;
import org.jboss.seam.jcr.test.common.ocm.BasicNode;

@JcrDao
public interface BasicNodeDAO {
	@JcrFind
	public BasicNode findBasicNode(String uuid);
	@JcrQuery(query="select * from [nt:unstructured]",language="JCR-SQL2",resultClass=BasicNode.class)
	public List<BasicNode> findAllNodes();
	@JcrSave
	public String save(String path, BasicNode basicNode);
}
