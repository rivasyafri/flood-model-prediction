package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.Temperature;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "temperature", path = "temperature")
public interface TemperatureRepository extends JpaSpecificationRepository<Temperature, Long> {
}
