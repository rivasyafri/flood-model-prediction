package com.mofp.flood.dao;

import com.mofp.flood.model.FloodArea;
import com.mofp.framework.dao.support.JpaSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rivasyafri
 */
@Repository
public interface FloodAreaRepository extends JpaSpecificationRepository<FloodArea, Long> {
}
