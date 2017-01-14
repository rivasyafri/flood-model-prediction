package com.mofp.flood.dao;

import com.mofp.flood.model.CellBalance;
import com.mofp.framework.dao.support.JpaSpecificationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "cell-balance", path = "cell-balance")
public interface CellBalanceRepository extends JpaSpecificationRepository<CellBalance, Long> {
}
