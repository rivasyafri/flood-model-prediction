package com.mofp.dao.data;

import com.mofp.dao.support.JpaSpecificationRepository;
import com.mofp.model.data.SoilType;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "soil-type", path = "soil-type")
public interface SoilTypeRepository extends JpaSpecificationRepository<SoilType, Long> {
}
