package com.mofp.service.impl;

import com.mofp.dao.CellRepository;
import com.mofp.dao.moving.CellHeightWaterRepository;
import com.mofp.dao.moving.CellStateRepository;
import com.mofp.model.Cell;
import com.mofp.model.Project;
import com.mofp.model.State;
import com.mofp.model.moving.CellHeightWater;
import com.mofp.model.moving.CellState;
import com.mofp.service.CellService;
import com.mofp.service.support.impl.DefaultBaseServiceImpl;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rivasyafri
 */
@Service
public class DefaultCellServiceImpl extends DefaultBaseServiceImpl<CellRepository> implements CellService {

    @Autowired
    private CellHeightWaterRepository cellHeightWaterRepository;

    @Autowired
    private CellStateRepository cellStateRepository;

    @Override
    public boolean removeCellFromProject(@NonNull Project project) {
        try {
            List<Cell> cells = project.getCells();
            if (cells != null) {
                repository.delete(cells);
                repository.flush();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Cell createOrUpdateHeightWaterRecord(Project project, Cell beforeCell, long time) {
        Cell cell = beforeCell;
        List<CellHeightWater> heightWaters = cellHeightWaterRepository.findByCellId(cell.getId());
        CellHeightWater cellHeightWater;
        if (heightWaters == null) {
            heightWaters = new ArrayList<>();
        }
        if (heightWaters.size() != 0) {
            cellHeightWater = heightWaters.get(heightWaters.size() - 1);
            cellHeightWater.setEndTime(time);
            cellHeightWaterRepository.save(cellHeightWater);
            heightWaters.set(heightWaters.size() - 1, cellHeightWater);
        }
        cellHeightWater = new CellHeightWater();
        cellHeightWater.setCell(cell);
        cellHeightWater.setValue(cell.getWaterHeight());
        cellHeightWater.setStartTime(time);
        cellHeightWater.setProject(project);
        cellHeightWaterRepository.save(cellHeightWater);
        heightWaters.add(cellHeightWater);
        cellHeightWaterRepository.flush();
        return cell;
    }

    @Override
    public Cell createOrUpdateCellStateRecord(Project project, Cell beforeCell, State updatedState, long time) {
        Cell cell = beforeCell;
        List<CellState> cellStates = cellStateRepository.findByCellId(cell.getId());
        CellState cellState;
        if (cellStates == null) {
            cellStates = new ArrayList<>();
        }
        if (cellStates.size() != 0) {
            cellState = cellStates.get(cellStates.size() - 1);
            cellState.setEndTime(time);
            cellStateRepository.save(cellState);
            cellStates.set(cellStates.size() - 1, cellState);
        }
        if (updatedState.isActive()) {
            cellState = new CellState();
            cellState.setCell(cell);
            cellState.setValue(updatedState);
            cellState.setStartTime(time);
            cellState.setProject(project);
            cellStateRepository.save(cellState);
            cellStates.add(cellState);
        }
        cellStateRepository.flush();
        return cell;
    }
}
