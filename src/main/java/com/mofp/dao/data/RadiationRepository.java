package com.mofp.dao.data;

import com.mofp.dao.support.JpaSpecificationRepository;
import com.mofp.model.data.Radiation;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "radiation", path = "radiation")
public interface RadiationRepository extends JpaSpecificationRepository<Radiation, Long> {
}
