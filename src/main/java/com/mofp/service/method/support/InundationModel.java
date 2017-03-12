package com.mofp.service.method.support;

import com.mofp.model.Cell;
import com.mofp.model.Neighborhood;
import com.mofp.model.Project;
import com.mofp.model.State;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author rivasyafri
 */
public abstract class InundationModel {

    public State WET_STATE;

    public State DRY_STATE;

    public abstract ArrayList<Cell> process(@NonNull AtomicReference<Project> projectReference, Cell cell)
            throws NullPointerException;

    protected ArrayList<Cell> getNeighborByCertainCell(@NonNull Project project, @NonNull Cell cell)
            throws NullPointerException {
        Neighborhood neighborhood = new Neighborhood();
        ArrayList<int[][]> NEIGHBOR = neighborhood.moore();
        int numberOfCellX = project.NUMBER_OF_CELL.getFirst();
        int numberOfCellY = project.NUMBER_OF_CELL.getSecond();
        int _x, _y;
        ArrayList<Cell> neighborCells = new ArrayList<>();
        for (int[][] i : NEIGHBOR) {
            for (int[] j : i) {
                _x = j[0] + cell.getXArray();
                _y = j[1] + cell.getYArray();
                if (_x >= 0 && _y >= 0 && _x < numberOfCellX && _y < numberOfCellY) {
                    Cell neighborCell = project.MATRIX[_y][_x];
                    neighborCells.add(neighborCell);
                }
            }
        }
        return neighborCells;
    }
}
