package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.RelativeHumidity;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "relative-humidity", path = "relative-humidity")
public interface RelativeHumidityRepository extends JpaSpecificationRepository<RelativeHumidity, Long> {
}
