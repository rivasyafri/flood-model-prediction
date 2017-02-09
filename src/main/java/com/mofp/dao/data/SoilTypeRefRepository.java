package com.mofp.dao.data;

import com.mofp.dao.support.JpaSpecificationRepository;
import com.mofp.model.data.SoilTypeRef;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "soil-type-ref", path = "soil-type-ref")
public interface SoilTypeRefRepository extends JpaSpecificationRepository<SoilTypeRef, Long> {
}
