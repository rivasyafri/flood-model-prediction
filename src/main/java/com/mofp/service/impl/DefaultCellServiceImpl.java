package com.mofp.service.impl;

import com.mofp.dao.CellRepository;
import com.mofp.dao.moving.CellHeightWaterRepository;
import com.mofp.dao.moving.CellStateRepository;
import com.mofp.model.Cell;
import com.mofp.model.Project;
import com.mofp.model.State;
import com.mofp.model.data.District;
import com.mofp.model.data.Weather;
import com.mofp.model.moving.CellHeightWater;
import com.mofp.model.moving.CellState;
import com.mofp.service.CellService;
import com.mofp.service.StateService;
import com.mofp.service.data.DistrictService;
import com.mofp.service.data.GoogleElevationService;
import com.mofp.service.support.impl.DefaultBaseServiceImpl;
import com.mofp.util.WktGenerator;
import lombok.NonNull;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author rivasyafri
 */
@Service
public class DefaultCellServiceImpl extends DefaultBaseServiceImpl<CellRepository> implements CellService {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private CellHeightWaterRepository cellHeightWaterRepository;

    @Autowired
    private CellStateRepository cellStateRepository;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private GoogleElevationService googleElevationService;

    @Autowired
    private StateService stateService;

    @Override
    public Cell createNewCellWithAllData(@NonNull Project project, @NonNull State state, int x, int y) {
        double latNorth = project.NORTH_WEST.getFirst();
        double longWest = project.NORTH_WEST.getSecond();
        double deltaX = project.DELTA.getFirst();
        double deltaY = project.DELTA.getSecond();
        Cell cell = new Cell(x, y, state);
        cell.setArea(WktGenerator.createSquareBasedBorder(
                latNorth + deltaY * y,
                longWest + deltaX * x,
                latNorth + deltaY * (y + 1),
                longWest + deltaX * (x + 1))
        );
        HashMap<Integer, Double> center = cell.getCenterPointOfArea();
        cell.setProject(project);
        cell.randomizeData(); // for experiment
        District district = districtService.findOneOrCreateNewDistrict(center.get(1), center.get(2));
        if (district != null) {
            List<Weather> weathers = district.getWeathers();
            if (weathers == null) {
                if (weathers.isEmpty()) {
                    district = districtService.findOneAndAddNewMovingObjectDataDistrict(district);
                }
            }
            cell.setDistrict(district);
        }
        cell = getRepository().saveAndFlush(cell);
        logger.debug(cell.toString());
        return cell;
    }

    @Override
    public ArrayList<Cell> updateHeightForCells(@NonNull ArrayList<Cell> cells) {
        ArrayList<Cell> updatedCells = googleElevationService.getElevationForAllCells(cells);
        updatedCells = new ArrayList<>(getRepository().save(updatedCells));
        getRepository().flush();
        return updatedCells;
    }

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
    public Cell updateCellByRunOff(@NonNull AtomicReference<Project> projectReference, @NonNull Cell beforeCell,
                                    double runOff, long timeElapsed) throws NullPointerException {
        Project project = projectReference.get();
        Cell cell = beforeCell;
        State wetState = stateService.findAndCreateWetState();
        State dryState = stateService.findAndCreateDryState();
        if (runOff > 0) {
            cell.setWaterHeight(runOff);
            cell.updateTotalHeight();
            if (cell.getTimeStartFlooded() == 0) {
                cell.setCurrentState(wetState);
                cell.setTimeStartFlooded(timeElapsed);
                cell = createOrUpdateCellStateRecord(project, cell, wetState, timeElapsed);
            }
            cell = createOrUpdateHeightWaterRecord(project, cell, timeElapsed);
            project.NEW_ACTIVE_CELLS.add(cell);
        } else if (cell.getWaterHeight() > 0) {
            cell.setWaterHeight(runOff);
            cell.updateTotalHeight();
            cell.setCurrentState(dryState);
            cell.setTimeStartFlooded(0);
            cell = createOrUpdateCellStateRecord(project, cell, dryState, timeElapsed);
        }
        return cell;
    }

    private Cell createOrUpdateHeightWaterRecord(@NonNull Project project, @NonNull Cell beforeCell, long time) {
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

    private Cell createOrUpdateCellStateRecord(@NonNull Project project, @NonNull Cell beforeCell,
                                               @NonNull State updatedState, long time) {
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
