package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.Precipitation;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "precipitation", path = "precipitation")
public interface PrecipitationRepository extends JpaSpecificationRepository<Precipitation, Long> {
}
