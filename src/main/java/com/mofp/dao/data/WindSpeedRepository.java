package com.mofp.dao.data;

import com.mofp.dao.support.JpaSpecificationRepository;
import com.mofp.model.data.WindSpeed;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "wind-speed", path = "wind-speed")
public interface WindSpeedRepository extends JpaSpecificationRepository<WindSpeed, Long> {
}
