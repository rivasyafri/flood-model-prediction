package com.mofp.dao.data;

import com.mofp.dao.support.JpaSpecificationRepository;
import com.mofp.model.data.Drainage;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "drainage", path = "drainage")
public interface DrainageRepository extends JpaSpecificationRepository<Drainage, Long> {
}
