package com.mofp.dao.moving;

import com.mofp.dao.support.JpaSpecificationRepository;
import com.mofp.model.moving.CellState;
import com.mofp.model.support.json.CellStateToStateProjectAndCellProjection;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "cell_state", path = "cell_state",
        excerptProjection = CellStateToStateProjectAndCellProjection.class)
public interface CellStateRepository extends JpaSpecificationRepository<CellState, Long> {

    List<CellState> findByCellId(@Param("id") Long cellId);
    @RestResource(path="findByProjectId")
    List<CellState> findByProjectId(@Param("id") Long projectId);
    @RestResource(path="findByProjectIdWithSorting")
    List<CellState> findByProjectId(@Param("id") Long projectId, Sort pageable);
}
