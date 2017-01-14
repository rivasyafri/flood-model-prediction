package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.Temperature;
import org.springframework.stereotype.Repository;

/**
 * @author rivasyafri
 */
@Repository
public interface TemperatureRepository extends JpaSpecificationRepository<Temperature, Long> {
}
