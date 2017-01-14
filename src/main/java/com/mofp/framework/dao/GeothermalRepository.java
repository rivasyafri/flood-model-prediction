package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.Geothermal;
import org.springframework.stereotype.Repository;

/**
 * @author rivasyafri
 */
@Repository
public interface GeothermalRepository extends JpaSpecificationRepository<Geothermal, Long> {
}
