package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.SoilType;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "soil-type", path = "soil-type")
public interface SoilTypeRepository extends JpaSpecificationRepository<SoilType, Long> {
}
