package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.WindSpeed;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "wind-speed", path = "wind-speed")
public interface WindSpeedRepository extends JpaSpecificationRepository<WindSpeed, Long> {
}
