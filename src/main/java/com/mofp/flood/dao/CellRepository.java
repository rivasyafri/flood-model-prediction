package com.mofp.flood.dao;

import com.mofp.flood.model.Cell;
import com.mofp.framework.dao.support.JpaSpecificationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "cell", path = "cell")
public interface CellRepository extends JpaSpecificationRepository<Cell, Long> {
}
