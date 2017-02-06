package com.mofp.dao.data;

import com.mofp.dao.support.JpaSpecificationRepository;
import com.mofp.model.data.LandCover;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "land-cover-ref", path = "land-cover-ref")
public interface LandCoverRepository extends JpaSpecificationRepository<LandCover, Long> {
}
