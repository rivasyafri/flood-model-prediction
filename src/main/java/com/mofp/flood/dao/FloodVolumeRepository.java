package com.mofp.flood.dao;

import com.mofp.flood.model.FloodVolume;
import com.mofp.framework.dao.support.JpaSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rivasyafri
 */
@Repository
public interface FloodVolumeRepository extends JpaSpecificationRepository<FloodVolume, Long> {
}
