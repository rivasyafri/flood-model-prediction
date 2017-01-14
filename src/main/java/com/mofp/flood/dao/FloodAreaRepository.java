package com.mofp.flood.dao;

import com.mofp.flood.model.FloodArea;
import com.mofp.framework.dao.support.JpaSpecificationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "flood-area", path = "flood-area")
public interface FloodAreaRepository extends JpaSpecificationRepository<FloodArea, Long> {
}
