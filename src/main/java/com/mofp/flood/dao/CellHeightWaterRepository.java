package com.mofp.flood.dao;

import com.mofp.flood.model.CellHeightWater;
import com.mofp.framework.dao.support.JpaSpecificationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "cell-height-water", path = "cell-height-water")
public interface CellHeightWaterRepository extends JpaSpecificationRepository<CellHeightWater, Long> {
}
