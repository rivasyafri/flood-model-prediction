package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.RelativeHumidity;
import org.springframework.stereotype.Repository;

/**
 * @author rivasyafri
 */
@Repository
public interface RelativeHumidityRepository extends JpaSpecificationRepository<RelativeHumidity, Long> {
}
