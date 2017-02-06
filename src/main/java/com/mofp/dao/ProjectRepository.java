package com.mofp.dao;

import com.mofp.model.Project;
import com.mofp.dao.support.JpaSpecificationRepository;
import com.mofp.model.support.json.inlineVariable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "project", path = "project", excerptProjection = inlineVariable.class)
public interface ProjectRepository extends JpaSpecificationRepository<Project, Long> {
}
