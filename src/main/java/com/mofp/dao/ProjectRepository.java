package com.mofp.dao;

import com.mofp.dao.support.JpaSpecificationRepository;
import com.mofp.model.Project;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "project", path = "project")
public interface ProjectRepository extends JpaSpecificationRepository<Project, Long> {
}
