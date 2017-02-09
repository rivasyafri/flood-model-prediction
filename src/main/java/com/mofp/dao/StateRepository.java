package com.mofp.dao;

import com.mofp.model.State;
import com.mofp.dao.support.JpaSpecificationRepository;
import com.mofp.model.support.json.CellStateToStateProjectAndCellProjection;
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
