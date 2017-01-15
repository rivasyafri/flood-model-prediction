package com.mofp.ca.dao;

import com.mofp.ca.model.Project;
import com.mofp.framework.dao.support.JpaSpecificationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "project", path = "project")
public interface ProjectRepository extends JpaSpecificationRepository<Project, Long> {
}
