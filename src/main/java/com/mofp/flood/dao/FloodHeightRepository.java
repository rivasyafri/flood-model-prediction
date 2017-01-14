package com.mofp.flood.dao;

import com.mofp.flood.model.FloodHeight;
import com.mofp.framework.dao.support.JpaSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rivasyafri
 */
@Repository
public interface FloodHeightRepository extends JpaSpecificationRepository<FloodHeight, Long> {
}
