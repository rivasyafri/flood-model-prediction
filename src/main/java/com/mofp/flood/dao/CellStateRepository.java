package com.mofp.flood.dao;

import com.mofp.flood.model.CellState;
import com.mofp.framework.dao.support.JpaSpecificationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "cell-state", path = "cell-state")
public interface CellStateRepository extends JpaSpecificationRepository<CellState, Long> {
}
