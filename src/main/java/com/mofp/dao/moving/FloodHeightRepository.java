package com.mofp.dao.moving;

import com.mofp.dao.support.JpaSpecificationRepository;
import com.mofp.model.moving.FloodHeight;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "flood-height", path = "flood-height")
public interface FloodHeightRepository extends JpaSpecificationRepository<FloodHeight, Long> {
}
