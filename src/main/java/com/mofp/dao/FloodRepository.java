package com.mofp.dao;

import com.mofp.model.Flood;
import com.mofp.dao.support.JpaSpecificationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "flood", path = "flood")
public interface FloodRepository extends JpaSpecificationRepository<Flood, Long> {
}
