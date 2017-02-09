package com.mofp.dao.moving;

import com.mofp.model.moving.CellHeightWater;
import com.mofp.dao.support.JpaSpecificationRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "cell-height-water", path = "cell-height-water")
public interface CellHeightWaterRepository extends JpaSpecificationRepository<CellHeightWater, Long> {

    List<CellHeightWater> findByCellId(@Param("cellId") Long cellId);
    List<CellHeightWater> findByProjectId(@Param("projectId") Long projectId);
}
