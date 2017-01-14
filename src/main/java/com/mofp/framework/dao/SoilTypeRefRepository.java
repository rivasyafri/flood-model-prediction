package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.SoilTypeRef;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "soil-type-ref", path = "soil-type-ref")
public interface SoilTypeRefRepository extends JpaSpecificationRepository<SoilTypeRef, Long> {
}
