package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.LandCoverRef;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "land-cover", path = "land-cover")
public interface LandCoverRefRepository extends JpaSpecificationRepository<LandCoverRef, Long> {
}
