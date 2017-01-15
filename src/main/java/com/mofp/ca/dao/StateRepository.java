package com.mofp.ca.dao;

import com.mofp.ca.model.State;
import com.mofp.framework.dao.support.JpaSpecificationRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "state", path = "state")
public interface StateRepository extends JpaSpecificationRepository<State, Long> {

    List<State> findByName(@Param("name") String name);
}
