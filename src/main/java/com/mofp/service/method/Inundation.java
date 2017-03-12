package com.mofp.service.method;

import com.mofp.model.Cell;
import com.mofp.model.Project;
import com.mofp.service.method.support.InundationModel;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Rule of process
 * @author rivasyafri
 */
public class Inundation extends InundationModel {

    @Override
    public ArrayList<Cell> process(@NonNull AtomicReference<Project> projectReference, Cell cell)
            throws NullPointerException {
        ArrayList<Cell> cells = new ArrayList<>();
        Project project = projectReference.get();
        ArrayList<Cell> neighborCells = getNeighborByCertainCell(project, cell);
        double averageOfTotalHeight = getAverageOfHeight(cell, neighborCells);
        ArrayList<AtomicReference<Cell>> processedNeighborCells = getProcessedNeighborByCertainCell(projectReference,
                cell, neighborCells, averageOfTotalHeight);
        double inundation = cell.getWaterHeight() / (processedNeighborCells.size() + 1);
        double deltaCenter = processInundationForProcessedNeighborCell(projectReference, cell, processedNeighborCells, inundation);
        cell.setWaterHeight(inundation + deltaCenter);
        cell.updateTotalHeight();
        cells.add(cell);
        for (AtomicReference<Cell> processedNeighborCell: processedNeighborCells) {
            Cell cell1 = processedNeighborCell.get();
            cells.add(cell1);
        }
        projectReference.set(project);
        return cells;
    }

    private double getAverageOfHeight(@NonNull Cell centerCell, @NonNull ArrayList<Cell> neighborCells) {
        double totalHeight = centerCell.getTotalHeight();
        for (Cell neighborCell : neighborCells) {
            totalHeight += neighborCell.getTotalHeight();
        }
        double averageOfTotalHeight = totalHeight / (neighborCells.size() + 1);
        return averageOfTotalHeight;
    }

    private ArrayList<AtomicReference<Cell>> getProcessedNeighborByCertainCell(@NonNull AtomicReference<Project> projectReference,
                                                                               @NonNull Cell cell,
                                                                               @NonNull ArrayList<Cell> neighborCells,
                                                                               double averageOfTotalHeight) {
        Project project = projectReference.get();
        ArrayList<AtomicReference<Cell>> processedNeighborCells = new ArrayList<>();
        for (Cell neighborCell : neighborCells) {
            if (neighborCell.getTotalHeight() < averageOfTotalHeight &&
                    neighborCell.getTotalHeight() < cell.getTotalHeight()) {
                AtomicReference<Cell> processedCell = new AtomicReference<>(
                        project.MATRIX[neighborCell.getYArray()][neighborCell.getXArray()]);
                processedNeighborCells.add(processedCell);
            }
        }
        return processedNeighborCells;
    }

    private double processInundationForProcessedNeighborCell(@NonNull AtomicReference<Project> projectReference,
                                                             @NonNull Cell cell,
                                                             @NonNull ArrayList<AtomicReference<Cell>> processedNeighborCells,
                                                             double inundation)
            throws NullPointerException{
        Project project = projectReference.get();
        double deltaCenter = 0;
        for (AtomicReference<Cell> processedNeighborCellReference : processedNeighborCells) {
            Cell processedNeighborCell = processedNeighborCellReference.get();
            if (processedNeighborCell.getTotalHeight() + inundation > cell.getTotalHeight()) {
                double delta = cell.getTotalHeight() - processedNeighborCell.getTotalHeight();
                processedNeighborCell.setWaterHeight(processedNeighborCell.getWaterHeight() + delta);
                deltaCenter += inundation - delta;
            } else {
                processedNeighborCell.setWaterHeight(processedNeighborCell.getWaterHeight() + inundation);
            }
            processedNeighborCell.setCurrentState(WET_STATE);
            processedNeighborCell.updateTotalHeight();
            processedNeighborCellReference.set(processedNeighborCell);
            if (!project.PROCESSED_CELLS.contains(processedNeighborCell)) {
                project.ACTIVE_CELLS.add(processedNeighborCell);
            }
        }
        projectReference.set(project);
        return deltaCenter;
    }
}
