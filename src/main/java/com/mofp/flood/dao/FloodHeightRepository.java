package com.mofp.flood.dao;

import com.mofp.flood.model.FloodHeight;
import com.mofp.framework.dao.support.JpaSpecificationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "flood-height", path = "flood-height")
public interface FloodHeightRepository extends JpaSpecificationRepository<FloodHeight, Long> {
}
