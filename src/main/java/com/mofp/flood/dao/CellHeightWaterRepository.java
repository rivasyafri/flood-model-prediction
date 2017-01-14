package com.mofp.flood.dao;

import com.mofp.flood.model.CellHeightWater;
import com.mofp.framework.dao.support.JpaSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rivasyafri
 */
@Repository
public interface CellHeightWaterRepository extends JpaSpecificationRepository<CellHeightWater, Long> {
}
