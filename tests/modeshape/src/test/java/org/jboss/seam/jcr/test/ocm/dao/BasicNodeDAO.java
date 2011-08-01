package org.jboss.seam.jcr.test.ocm.dao;

import static org.jboss.seam.jcr.ConfigParams.MODESHAPE_URL;

import org.jboss.seam.jcr.annotations.JcrConfiguration;
import org.jboss.seam.jcr.annotations.ocm.OCMDao;
import org.jboss.seam.jcr.annotations.ocm.OCMFind;
import org.jboss.seam.jcr.test.ocm.BasicNode;

@OCMDao(
	@JcrConfiguration(name = MODESHAPE_URL, 
		value = "file:target/test-classes/modeshape.xml?repositoryName=CarRepo")
)
public interface BasicNodeDAO {
	@OCMFind
	public BasicNode findBasicNode(String uuid);
}
