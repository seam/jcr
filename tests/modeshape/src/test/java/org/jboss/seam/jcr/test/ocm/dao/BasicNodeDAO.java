package org.jboss.seam.jcr.test.ocm.dao;

import static org.jboss.seam.jcr.ConfigParams.MODESHAPE_URL;

import java.util.List;

import org.jboss.seam.jcr.annotations.JcrConfiguration;
import org.jboss.seam.jcr.annotations.ocm.JcrDao;
import org.jboss.seam.jcr.annotations.ocm.JcrFind;
import org.jboss.seam.jcr.annotations.ocm.JcrQuery;
import org.jboss.seam.jcr.annotations.ocm.JcrSave;
import org.jboss.seam.jcr.test.ocm.BasicNode;

@JcrDao(
	@JcrConfiguration(name = MODESHAPE_URL, 
		value = "file:target/test-classes/modeshape.xml?repositoryName=CarRepo")
)
public interface BasicNodeDAO {
	@JcrFind
	public BasicNode findBasicNode(String uuid);
	@JcrQuery(query="select * from [nt:unstructured]",language="JCR-SQL2",resultClass=BasicNode.class)
	public List<BasicNode> findAllNodes();
	@JcrSave
	public String save(String path, BasicNode basicNode);
}
