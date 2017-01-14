package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.Precipitation;
import org.springframework.stereotype.Repository;

/**
 * @author rivasyafri
 */
@Repository
public interface PrecipitationRepository extends JpaSpecificationRepository<Precipitation, Long> {
}
