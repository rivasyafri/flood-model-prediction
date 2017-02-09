package com.mofp.dao.data;

import com.mofp.dao.support.JpaSpecificationRepository;
import com.mofp.model.data.LandCoverRef;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "land-cover", path = "land-cover")
public interface LandCoverRefRepository extends JpaSpecificationRepository<LandCoverRef, Long> {
}
