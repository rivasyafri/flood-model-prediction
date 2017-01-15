package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.Topography;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "topography", path = "topography")
public interface TopographyRepository extends JpaSpecificationRepository<Topography, Long> {
}
