package com.mofp.dao.data;

import com.mofp.dao.support.JpaSpecificationRepository;
import com.mofp.model.data.Geothermal;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "geothermal", path = "geothermal")
public interface GeothermalRepository extends JpaSpecificationRepository<Geothermal, Long> {
}
