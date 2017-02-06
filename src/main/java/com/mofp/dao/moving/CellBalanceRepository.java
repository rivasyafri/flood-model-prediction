package com.mofp.dao.moving;

import com.mofp.model.moving.CellBalance;
import com.mofp.dao.support.JpaSpecificationRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author rivasyafri
 */
@RepositoryRestResource(collectionResourceRel = "cell-balance", path = "cell-balance")
public interface CellBalanceRepository extends JpaSpecificationRepository<CellBalance, Long> {
}
