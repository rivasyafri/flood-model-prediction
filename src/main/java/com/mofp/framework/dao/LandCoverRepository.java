package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.LandCover;
import org.springframework.stereotype.Repository;

/**
 * @author rivasyafri
 */
@Repository
public interface LandCoverRepository extends JpaSpecificationRepository<LandCover, Long> {
}
