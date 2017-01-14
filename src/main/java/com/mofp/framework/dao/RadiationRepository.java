package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.Radiation;
import org.springframework.stereotype.Repository;

/**
 * @author rivasyafri
 */
@Repository
public interface RadiationRepository extends JpaSpecificationRepository<Radiation, Long> {
}
