package com.mofp.dao.data;

import com.mofp.dao.support.JpaSpecificationRepository;
import com.mofp.model.data.Temperature;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "temperature", path = "temperature")
public interface TemperatureRepository extends JpaSpecificationRepository<Temperature, Long> {
}
