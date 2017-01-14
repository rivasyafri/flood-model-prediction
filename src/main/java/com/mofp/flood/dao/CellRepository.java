package com.mofp.flood.dao;

import com.mofp.flood.model.Cell;
import com.mofp.framework.dao.support.JpaSpecificationRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rivasyafri
 */
@Repository
public interface CellRepository extends JpaSpecificationRepository<Cell, Long> {
}
