package com.mofp.flood.dao;

import com.mofp.flood.model.CellState;
import com.mofp.framework.dao.support.JpaSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rivasyafri
 */
@Repository
public interface CellStateRepository extends JpaSpecificationRepository<CellState, Long> {
}
