package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.WindSpeed;
import org.springframework.stereotype.Repository;

/**
 * @author rivasyafri
 */
@Repository
public interface WindSpeedRepository extends JpaSpecificationRepository<WindSpeed, Long> {
}
