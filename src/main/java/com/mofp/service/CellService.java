package com.mofp.service;

import com.mofp.dao.CellRepository;
import com.mofp.model.Cell;
import com.mofp.model.Project;
import com.mofp.model.State;
import com.mofp.service.support.AbstractBaseService;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author rivasyafri
 */
public interface CellService extends AbstractBaseService<CellRepository> {

    /**
     * Create new cell with certain condition
     * @param project Project must be initialized first
     * @param state dry state
     * @param x x in array
     * @param y y in array
     * @return cell with certain condition
     */
    Cell createNewCellWithAllData(@NonNull Project project, @NonNull State state, int x, int y);

    /**
     * Update cells' height using google elevation
     * @param cells list of cells
     * @return updated cells
     */
    ArrayList<Cell> updateHeightForCells(@NonNull ArrayList<Cell> cells);

    /**
     * Delete all cells from project
     * @param project not null
     * @return true if success
     */
    boolean removeCellFromProject(@NonNull Project project);

    /**
     * Update cell state and height water record of certain cell
     * @param projectReference reference of project
     * @param beforeCell cell before
     * @param runOff
     * @param timeElapsed
     * @return cell after
     * @throws NullPointerException
     */
    Cell updateCellByRunOff(@NonNull AtomicReference<Project> projectReference, @NonNull Cell beforeCell,
                                   double runOff, long timeElapsed) throws NullPointerException;
}
