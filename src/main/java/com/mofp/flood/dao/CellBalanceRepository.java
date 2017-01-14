package com.mofp.flood.dao;

import com.mofp.flood.model.CellBalance;
import com.mofp.framework.dao.support.JpaSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rivasyafri
 */
@Repository
public interface CellBalanceRepository extends JpaSpecificationRepository<CellBalance, Long> {
}
