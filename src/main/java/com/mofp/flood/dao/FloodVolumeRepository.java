package com.mofp.flood.dao;

import com.mofp.flood.model.FloodVolume;
import com.mofp.framework.dao.support.JpaSpecificationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "flood-volume", path = "flood-volume")
public interface FloodVolumeRepository extends JpaSpecificationRepository<FloodVolume, Long> {
}
