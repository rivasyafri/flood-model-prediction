package com.mofp.dao.data;

import com.mofp.dao.support.JpaSpecificationRepository;
import com.mofp.model.data.RelativeHumidity;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "relative-humidity", path = "relative-humidity")
public interface RelativeHumidityRepository extends JpaSpecificationRepository<RelativeHumidity, Long> {
}
