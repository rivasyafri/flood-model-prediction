package com.mofp.ca.dao;

import com.mofp.ca.model.Cell;
import com.mofp.framework.dao.support.JpaSpecificationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "cell", path = "cell")
public interface CellRepository extends JpaSpecificationRepository<Cell, Long> {
}
