package com.mofp.dao;

import com.mofp.model.Cell;
import com.mofp.dao.support.JpaSpecificationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "cell", path = "cell")
public interface CellRepository extends JpaSpecificationRepository<Cell, Long> {

    List<Cell> findByProjectId(Long projectId);
}
