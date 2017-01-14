package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.SoilTypeRef;
import org.springframework.stereotype.Repository;

/**
 * @author rivasyafri
 */
@Repository
public interface SoilTypeRefRepository extends JpaSpecificationRepository<SoilTypeRef, Long> {
}
