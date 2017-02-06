package com.mofp.dao.data;

import com.mofp.dao.support.JpaSpecificationRepository;
import com.mofp.model.data.Precipitation;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "precipitation", path = "precipitation")
public interface PrecipitationRepository extends JpaSpecificationRepository<Precipitation, Long> {
}
