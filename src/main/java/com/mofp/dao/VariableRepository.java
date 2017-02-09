package com.mofp.dao;

import com.mofp.model.Variable;
import com.mofp.dao.support.JpaSpecificationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "variable", path = "variable")
public interface VariableRepository extends JpaSpecificationRepository<Variable, Long> {
}
