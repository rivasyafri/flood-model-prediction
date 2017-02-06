package com.mofp.dao.moving;

import com.mofp.model.moving.CellState;
import com.mofp.dao.support.JpaSpecificationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "cell-state", path = "cell-state")
public interface CellStateRepository extends JpaSpecificationRepository<CellState, Long> {
}
