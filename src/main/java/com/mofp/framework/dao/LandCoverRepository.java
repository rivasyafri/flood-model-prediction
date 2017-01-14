package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.LandCover;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "land-cover-ref", path = "land-cover-ref")
public interface LandCoverRepository extends JpaSpecificationRepository<LandCover, Long> {
}
