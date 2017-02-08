package com.mofp.dao.moving;

import com.mofp.dao.support.JpaSpecificationRepository;
import com.mofp.model.moving.CellState;
import com.mofp.model.support.json.CellStateToStateProjectAndCellProjection;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "cell-state", path = "cell-state",
        excerptProjection = CellStateToStateProjectAndCellProjection.class)
public interface CellStateRepository extends JpaSpecificationRepository<CellState, Long> {

    List<CellState> findByCellId(@Param("cellId") Long cellId);
    List<CellState> findByProjectId(@Param("projectId") Long projectId);
}
