package com.mofp.dao.moving;

import com.mofp.model.moving.FloodArea;
import com.mofp.dao.support.JpaSpecificationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "flood-area", path = "flood-area")
public interface FloodAreaRepository extends JpaSpecificationRepository<FloodArea, Long> {
}
