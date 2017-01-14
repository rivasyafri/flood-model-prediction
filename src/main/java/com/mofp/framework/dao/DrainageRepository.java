package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.Drainage;
import org.springframework.stereotype.Repository;

/**
 * @author rivasyafri
 */
@Repository
public interface DrainageRepository extends JpaSpecificationRepository<Drainage, Long> {
}
