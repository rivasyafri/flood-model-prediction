package com.mofp.service.impl;

import com.mofp.dao.CellRepository;
import com.mofp.dao.ProjectRepository;
import com.mofp.dao.StateRepository;
import com.mofp.dao.moving.CellHeightWaterRepository;
import com.mofp.dao.moving.CellStateRepository;
import com.mofp.model.Cell;
import com.mofp.model.Neighborhood;
import com.mofp.model.Project;
import com.mofp.model.State;
import com.mofp.model.moving.CellHeightWater;
import com.mofp.model.moving.CellState;
import com.mofp.service.GlobalService;
import com.mofp.service.ProjectService;
import com.mofp.service.method.ChenModel;
import com.mofp.service.method.PrasetyaModel;
import com.mofp.service.method.VICModel;
import com.mofp.service.method.formula.WaterBalance;
import com.mofp.service.method.support.FloodModel;
import org.apache.log4j.Logger;
import org.geolatte.geom.G2D;
import org.geolatte.geom.LineString;
import org.geolatte.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author rivasyafri
 * Edited from Global.class from Taufiqurrahman
 */
@Service
public class DefaultGlobalServiceImpl implements GlobalService {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CellHeightWaterRepository cellHeightWaterRepository;

    @Autowired
    private CellStateRepository cellStateRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CellRepository cellRepository;

    ////*********************** Model Properties ******************************** ////
    private double SIZE_X;
    private double SIZE_Y;
    private double DELTA_X;
    private double DELTA_Y;
    private int NUMBER_OF_CELL_X;
    private int NUMBER_OF_CELL_Y;

    private int TIME_STEP;
    private int TIME_ELAPSED;
    private int INTERVAL;

    private FloodModel selectedModel;

    private double latNorth;
    private double longWest;

    //// ************************** Neighborhood *************************************** ////
    private Neighborhood NEIGHBORHOOD;
    private ArrayList<int[][]> NEIGHBOR;

    //// ************************** Public Variables *************************************** ////
    private Project PROJECT;
    private Cell[][] MATRIX;
    private ArrayList<Cell> PROCESSED_CELLS;
    private PriorityQueue<Cell> ACTIVE_CELLS;
    private PriorityQueue<Cell> NEW_ACTIVE_CELLS;
    private Random RANDOM_GENERATOR;

    //// ************************** Constant State *************************************** ////
    private State WET_STATE;
    private State DRY_STATE;

    public Project run(String selectedModel, Project project) {
        this.init(selectedModel, project);
        projectRepository.save(PROJECT);
        for (TIME_ELAPSED = 0; TIME_ELAPSED < INTERVAL; TIME_ELAPSED = TIME_ELAPSED + TIME_STEP) {
            logger.debug("Time elapsed: " + TIME_ELAPSED + " from " + INTERVAL);
            logger.debug("Start inundation from all active cells : " + ACTIVE_CELLS.size() + "cells");
            while (ACTIVE_CELLS.size() != 0) {
                Cell activeCell = ACTIVE_CELLS.poll();
                PROCESSED_CELLS.add(activeCell);
                MATRIX[activeCell.getYArray()][activeCell.getXArray()] =
                        inundation(MATRIX[activeCell.getYArray()][activeCell.getXArray()]);
            }
            logger.debug("Ending inundation from all active cells : " + ACTIVE_CELLS.size() + "cells");
            logger.debug("Start iterate all cell");
            iterateAllCell(TIME_ELAPSED, TIME_STEP);
            logger.debug("End iterate all cell");
            ACTIVE_CELLS = NEW_ACTIVE_CELLS;
            PROCESSED_CELLS = new ArrayList<>();
            NEW_ACTIVE_CELLS = new PriorityQueue<>();
        }
        return PROJECT;
    }

    private void init(String selectedModel, Project PROJECT) {
        logger.debug("Initiation for GLOBAL start");
        this.PROJECT = PROJECT;
        TIME_STEP = PROJECT.getTimeStep();
        INTERVAL = PROJECT.getInterval();
        logger.debug("Initiation for GLOBAL area start");
        initArea();
        logger.debug("Initiation for GLOBAL area end");
        logger.debug("Initiation for GLOBAL matrix start");
        initMatrix();
        logger.debug("Initiation for GLOBAL matrix end");
        logger.debug("Initiation for GLOBAL model start");
        initModel(selectedModel);
        logger.debug("Initiation for GLOBAL model end");
        logger.debug("Initiation for GLOBAL state start");
        initState();
        logger.debug("Initiation for GLOBAL state end");
        logger.debug("Initiation for GLOBAL variable start");
        initVariables();
        logger.debug("Initiation for GLOBAL variable end");
        logger.debug("Initiation for GLOBAL end");
    }
    private void initArea() {
        double lat1, lat2 = 0, lon1, lon2 = 0;
        LineString<G2D> line = PROJECT.getArea().getExteriorRing();
        lat1 = line.getPositionN(0).getLat();
        lon1 = line.getPositionN(0).getLon();
        for (int i = 1; i < line.getNumPositions() - 1; i++) {
            G2D position = line.getPositionN(i);
            lat2 = lat1 != position.getLat() ? position.getLat() : lat2;
            lon2 = lon1 != position.getLon() ? position.getLon() : lon2;
        }
        latNorth = lat1 > lat2 ? lat1 : lat2;
        longWest = lon1 > lon2 ? lon2 : lon1;
        SIZE_X = Math.abs(lon1 - lon2);
        SIZE_Y = Math.abs(lat1 - lat2);
        DELTA_X = PROJECT != null ? convertMToLat(PROJECT.getCellSize()) : convertMToLat(1000);
        DELTA_Y = PROJECT != null ? convertMToLon(PROJECT.getCellSize(), lon1) : convertMToLon(1000, lon1);
    }
    private void initMatrix() {
        Long gridX = Math.round(SIZE_X / DELTA_X);
        Long gridY = Math.round(SIZE_Y / DELTA_Y);
        NUMBER_OF_CELL_X = gridX.intValue();
        NUMBER_OF_CELL_Y = gridY.intValue();
        MATRIX = new Cell[NUMBER_OF_CELL_Y][NUMBER_OF_CELL_X];
        ArrayList<Cell> cells = new ArrayList<>();
        for (int y = 0; y < NUMBER_OF_CELL_Y; y++) {
            for (int x = 0; x < NUMBER_OF_CELL_X; x++) {
                Cell cell = new Cell(x, y, DRY_STATE);
                cell.setArea(projectService.createRectangleFromBounds(
                        latNorth + latNorth * y,
                        longWest + longWest * x,
                        latNorth + latNorth * (y+1),
                        longWest + longWest * (x + 1)
                ));
                cell.randomizeData(); // for experiment
                cell = cellRepository.save(cell);
                MATRIX[y][x] = cell;
                cells.add(cell);
            }
        }
        cellRepository.flush();
        PROJECT.setCells(cells);
    }
    private void initModel(String selectedModel) {
        if (selectedModel.compareTo(PrasetyaModel.getModelName()) == 0) {
            this.selectedModel = new PrasetyaModel();
        } else if (selectedModel.compareTo(ChenModel.getModelName()) == 0) {
            this.selectedModel = new ChenModel();
        } else if (selectedModel.compareTo(VICModel.getModelName()) == 0) {
            this.selectedModel = new VICModel();
        } else {
            this.selectedModel = new PrasetyaModel();
        }
    }
    private void initState() {
        WET_STATE = stateRepository.findByName("WET").get(0);
        DRY_STATE = stateRepository.findByName("DRY").get(0);
    }
    private void initVariables() {
        NEIGHBORHOOD = new Neighborhood();
        NEIGHBOR = NEIGHBORHOOD.use("moore");
        ACTIVE_CELLS = new PriorityQueue<>();
        NEW_ACTIVE_CELLS = new PriorityQueue<>();
        PROCESSED_CELLS = new ArrayList<>();
        RANDOM_GENERATOR = new Random();
    }

    private void iterateAllCell(int timeElapsed, int timeStep) {
        for (int y = 0; y < MATRIX.length; y++) {
            for (int x = 0; x < MATRIX[0].length; x++) {
                double precipitation = getPrecipitation();
                int time = getDeltaTime(MATRIX[y][x], timeElapsed, timeStep);
                double runOff = selectedModel.calculateRunOff(PROJECT.getVariable(), MATRIX[y][x],
                        precipitation, time);
                double waterBalance = WaterBalance.calculate(MATRIX[y][x].getWaterBalanceBefore(),
                        precipitation, runOff, timeElapsed);
                MATRIX[y][x].setWaterBalanceAfter(waterBalance);
                MATRIX[y][x] = updateCellByRunOff(MATRIX[y][x], runOff, timeElapsed);
            }
        }
    }
    private Cell inundation(Cell cell) {
        int _x, _y;
        ArrayList<Cell> neighborCells = new ArrayList<>();
        for (int[][] i : NEIGHBOR) {
            for (int[] j : i) {
                _x = j[0] + cell.getXArray();
                _y = j[1] + cell.getYArray();
                if (_x >= 0 && _y >= 0 && _x < NUMBER_OF_CELL_X && _y < NUMBER_OF_CELL_Y) {
                    Cell neighborCell = MATRIX[_y][_x];
                    neighborCells.add(neighborCell);
                }
            }
        }
        double totalHeight = cell.getTotalHeight();
        for (Cell neighborCell : neighborCells) {
            totalHeight += neighborCell.getTotalHeight();
        }
        double averageOfTotalHeight = totalHeight / (neighborCells.size() + 1);

        ArrayList<AtomicReference<Cell>> processedNeighborCells = new ArrayList<>();
        for (Cell neighborCell: neighborCells) {
            if (neighborCell.getTotalHeight() < averageOfTotalHeight &&
                    neighborCell.getTotalHeight() < cell.getTotalHeight()) {
                AtomicReference<Cell> processedCell = new AtomicReference<>(
                        MATRIX[neighborCell.getYArray()][neighborCell.getXArray()]);
                processedNeighborCells.add(processedCell);
            }
        }

        double inundation = cell.getWaterHeight() / (processedNeighborCells.size() + 1);
        double deltaCenter = 0;
        for (AtomicReference<Cell> processedNeighborCellReference: processedNeighborCells) {
            Cell processedNeighborCell = processedNeighborCellReference.get();
            if (processedNeighborCell.getTotalHeight() + inundation > cell.getTotalHeight()) {
                double delta = cell.getTotalHeight() - processedNeighborCell.getTotalHeight();
                processedNeighborCell.setWaterHeight(processedNeighborCell.getWaterHeight() + delta);
                deltaCenter += inundation - delta;
            } else {
                processedNeighborCell.setWaterHeight(processedNeighborCell.getWaterHeight() + inundation);
            }
            processedNeighborCell.updateTotalHeight();
            processedNeighborCellReference.set(processedNeighborCell);
            if (!PROCESSED_CELLS.contains(processedNeighborCell)) {
                ACTIVE_CELLS.add(processedNeighborCell);
            }
        }
        cell.setWaterHeight(inundation + deltaCenter);
        cell.updateTotalHeight();
        return cell;
    }

    private Cell updateCellByRunOff(Cell beforeCell, double runOff, int timeElapsed) {
        Cell cell = beforeCell;
        if (runOff > 0) {
            cell.setWaterHeight(runOff);
            cell.updateTotalHeight();
            if (cell.getTimeStartFlooded() == 0) {
                cell.setCurrentState(WET_STATE);
                cell.setTimeStartFlooded(timeElapsed);
                cell = createOrUpdateCellStateRecord(cell, WET_STATE, timeElapsed);
            }
            cell = createOrUpdateHeightWaterRecord(cell, timeElapsed);
            NEW_ACTIVE_CELLS.add(cell);
        } else if (cell.getWaterHeight() > 0) {
            cell.setWaterHeight(runOff);
            cell.updateTotalHeight();
            cell.setCurrentState(DRY_STATE);
            cell.setTimeStartFlooded(0);
            cell = createOrUpdateCellStateRecord(cell, DRY_STATE, timeElapsed);
        }
        return cell;

    }
    private Cell createOrUpdateHeightWaterRecord(Cell beforeCell, int time) {
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
        cellHeightWater.setProject(PROJECT);
        cellHeightWaterRepository.save(cellHeightWater);
        heightWaters.add(cellHeightWater);
        cellHeightWaterRepository.flush();
        return cell;
    }
    private Cell createOrUpdateCellStateRecord(Cell beforeCell, State updatedState, int time) {
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
            cellState.setProject(PROJECT);
            cellStateRepository.save(cellState);
            cellStates.add(cellState);
        }
        cellStateRepository.flush();
        return cell;
    }

    private int getDeltaTime(Cell cell, int timeElapsed, int timeStep) {
        int time = 0;
        if (cell.getTimeStartFlooded() != 0) {
            time = timeElapsed - cell.getTimeStartFlooded();
        }
        time += timeStep;
        return time;
    }
    private double getPrecipitation() {
        return RANDOM_GENERATOR.nextDouble();
    }

    /* Get from http://stackoverflow.com/questions/1253499/simple-calculations-for-working-with-lat-lon-km-distance */
    private double convertMToLat(double m) {
        return (m/1000) / 110.574;
    }
    private double convertMToLon(double m, double latInDegree) {
        return (m/1000) * Math.acos(toRadians(latInDegree))/ 111.320;
    }
    /* Get from http://stackoverflow.com/questions/9705123/how-can-i-get-sin-cos-and-tan-to-use-degrees-instead-of-radians */
    private double toRadians (double angle) {
        return angle * (Math.PI / 180);
    }
}
