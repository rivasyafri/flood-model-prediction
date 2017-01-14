package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.SoilType;
import org.springframework.stereotype.Repository;

/**
 * @author rivasyafri
 */
@Repository
public interface SoilTypeRepository extends JpaSpecificationRepository<SoilType, Long> {
}
