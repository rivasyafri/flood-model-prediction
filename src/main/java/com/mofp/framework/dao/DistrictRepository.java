package com.mofp.framework.dao;

import com.mofp.framework.dao.support.JpaSpecificationRepository;
import com.mofp.framework.model.District;
import org.springframework.stereotype.Repository;

/**
 * @author rivasyafri
 */
@Repository
public interface DistrictRepository extends JpaSpecificationRepository<District, Long> {

}
