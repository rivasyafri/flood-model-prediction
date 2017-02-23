package com.mofp.dao.moving;

import com.mofp.dao.support.JpaSpecificationRepository;
import com.mofp.model.moving.CellHeightWater;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "cell-height-water", path = "cell-height-water")
public interface CellHeightWaterRepository extends JpaSpecificationRepository<CellHeightWater, Long> {

    List<CellHeightWater> findByCellId(@Param("id") Long cellId);
    @RestResource(path="findByProjectId")
    List<CellHeightWater> findByProjectId(@Param("id") Long projectId);
    @RestResource(path="findByProjectIdWithSorting")
    List<CellHeightWater> findByProjectId(@Param("id") Long projectId, Sort pageable);
}
