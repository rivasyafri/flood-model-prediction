package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.Topography;
import org.springframework.stereotype.Repository;

/**
 * @author rivasyafri
 */
@Repository
public interface TopographyRepository extends JpaSpecificationRepository<Topography, Long> {
}
