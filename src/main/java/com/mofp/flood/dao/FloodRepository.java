package com.mofp.flood.dao;

import com.mofp.flood.model.Flood;
import com.mofp.framework.dao.support.JpaSpecificationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "flood", path = "flood")
public interface FloodRepository extends JpaSpecificationRepository<Flood, Long> {
}
