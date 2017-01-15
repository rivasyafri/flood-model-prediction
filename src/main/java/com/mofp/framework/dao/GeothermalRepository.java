package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.Geothermal;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "geothermal", path = "geothermal")
public interface GeothermalRepository extends JpaSpecificationRepository<Geothermal, Long> {
}
