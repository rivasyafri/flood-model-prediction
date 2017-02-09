package com.mofp.dao.moving;

import com.mofp.model.moving.FloodVolume;
import com.mofp.dao.support.JpaSpecificationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "flood-volume", path = "flood-volume")
public interface FloodVolumeRepository extends JpaSpecificationRepository<FloodVolume, Long> {
}
