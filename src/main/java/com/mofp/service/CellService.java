package com.mofp.service;

import com.mofp.dao.CellRepository;
import com.mofp.model.Cell;
import com.mofp.model.Project;
import com.mofp.model.State;
import com.mofp.service.support.AbstractBaseService;
import lombok.NonNull;

/**
 * @author rivasyafri
 */
public interface CellService extends AbstractBaseService<CellRepository> {

    /**
     * Delete all cells from project
     * @param project not null
     * @return true if success
     */
    boolean removeCellFromProject(@NonNull Project project);

    /**
     * Update height water record of certain cell
     * @param project project value
     * @param beforeCell initial value of the cell
     * @param time unix time of time elapsed
     * @return updated cell
     */
    Cell createOrUpdateHeightWaterRecord(Project project, Cell beforeCell, long time);

    /**
     * Update cell state record of certain cell
     * @param project project value
     * @param beforeCell initial value of the cell
     * @param time unix time of time elapsed
     * @return updated cell
     */
    Cell createOrUpdateCellStateRecord(Project project, Cell beforeCell, State updatedState, long time);
}
