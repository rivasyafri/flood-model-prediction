package com.mofp.flood.dao;

import com.mofp.flood.model.Flood;
import com.mofp.framework.dao.support.JpaSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rivasyafri
 */
@Repository
public interface FloodRepository extends JpaSpecificationRepository<Flood, Long> {
}
