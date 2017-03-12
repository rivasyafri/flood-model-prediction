package com.mofp.service.impl;

import com.mofp.dao.ProjectRepository;
import com.mofp.model.*;
import com.mofp.model.data.District;
import com.mofp.model.data.Weather;
import com.mofp.service.CellService;
import com.mofp.service.ProjectService;
import com.mofp.service.StateService;
import com.mofp.service.data.DistrictService;
import com.mofp.service.method.ChenModel;
import com.mofp.service.method.Inundation;
import com.mofp.service.method.PrasetyaModel;
import com.mofp.service.method.VICModel;
import com.mofp.service.support.impl.DefaultBaseServiceImpl;
import lombok.NonNull;
import org.apache.log4j.Logger;
import org.geolatte.geom.G2D;
import org.geolatte.geom.LineString;
import org.geolatte.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author rivasyafri
 */
@Service
public class DefaultProjectServiceImpl extends DefaultBaseServiceImpl<ProjectRepository> implements ProjectService {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private DistrictService districtService;

    @Autowired
    private StateService stateService;

    @Autowired
    private CellService cellService;

    @Override
    public Project checkOrRunPredictionProcess(long id)
            throws IllegalAccessException, NullPointerException {
        try {
            Project project = repository.findOne(id);
            if (project.isDone() && project.getCellStates().size() != 0) {
                return project;
            }
            if (project.getArea() != null) {
                AtomicReference<Project> projectReference = new AtomicReference<>(project);
                initProject(projectReference);
                runPredictionProcess(projectReference);
                project = projectReference.get();
                project = repository.saveAndFlush(project);
                return project;
            } else {
                throw new IllegalAccessException("Area is not specified");
            }
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Project resetProject(long id) {
        Project project = repository.getOne(id);
        project.setDone(false);
        repository.saveAndFlush(project);
        boolean success = cellService.removeCellFromProject(project);
        if (success) {
            project = repository.getOne(project.getId());
            return project;
        }
        return null;
    }


    // Region of Method to initiate prediction
    private void initProject(@NonNull AtomicReference<Project> projectReference)
            throws NullPointerException {
        logger.debug("Initiation start");
        logger.debug("Initiation for global variable from area start");
        initGlobalPairVariablesFromArea(projectReference);
        logger.debug("Initiation for global variable from area end");
        logger.debug("Initiation for flood model start");
        initSelectedFloodModel(projectReference);
        logger.debug("Initiation for flood model end");
        logger.debug("Initiation for matrix cellular automata start");
        initCellsOnProject(projectReference);
        logger.debug("Initiation for matrix cellular automata end");
        logger.debug("Initiation end");
    }

    private void initSelectedFloodModel(@NonNull AtomicReference<Project> projectReference)
            throws IllegalArgumentException, NullPointerException {
        Project project = projectReference.get();
        String selectedModel = project.getModel();
        logger.debug("selectedModel = " + selectedModel);
        logger.debug("Prasetya model = " + PrasetyaModel.getModelName());
        logger.debug("Chen model = " + ChenModel.getModelName());
        logger.debug("VIC model = " + VICModel.getModelName());
        if (selectedModel.contains(PrasetyaModel.getModelName())) {
            project.SELECTED_MODEL = new PrasetyaModel();
        } else if (selectedModel.contains(ChenModel.getModelName())) {
            project.SELECTED_MODEL = new ChenModel();
        } else if (selectedModel.contains(VICModel.getModelName())) {
            project.SELECTED_MODEL = new VICModel();
        } else {
            throw new IllegalArgumentException("There is no model selected by that name");
        }
        project.INUNDATION_MODEL = new Inundation();
        project.INUNDATION_MODEL.WET_STATE = stateService.findAndCreateWetState();
        project.INUNDATION_MODEL.DRY_STATE = stateService.findAndCreateDryState();
        projectReference.set(project);
    }

    private void initGlobalPairVariablesFromArea(@NonNull AtomicReference<Project> projectReference)
            throws InvalidParameterException, NullPointerException {
        Project project = projectReference.get();
        Polygon<G2D> area = project.getArea();
        int cellSize = project.getCellSize();
        if (area == null) {
            throw new InvalidParameterException("area cannot be null");
        } else {
            double lat1, lat2 = 0, lon1, lon2 = 0;
            LineString<G2D> line = area.getExteriorRing();
            lat1 = line.getPositionN(0).getLat();
            lon1 = line.getPositionN(0).getLon();
            for (int i = 1; i < line.getNumPositions() - 1; i++) {
                G2D position = line.getPositionN(i);
                lat2 = lat1 != position.getLat() ? position.getLat() : lat2;
                lon2 = lon1 != position.getLon() ? position.getLon() : lon2;
            }
            double avgLat = (lat1 + lat2) / 2;
            project.NORTH_WEST = Pair.of(lat1 > lat2 ? lat1 : lat2, lon1 > lon2 ? lon2 : lon1);
            project.SIZE = Pair.of(Math.abs(lon1 - lon2), Math.abs(lat1 - lat2));
            project.DELTA = Pair.of(convertMToLon(cellSize, avgLat), convertMToLat(cellSize));
            Long gridX = Math.round(project.SIZE.getFirst() / project.DELTA.getFirst());
            Long gridY = Math.round(project.SIZE.getSecond() / project.DELTA.getSecond());
            project.NUMBER_OF_CELL = Pair.of(gridX.intValue(), gridY.intValue());
        }
        projectReference.set(project);
    }

    private void initCellsOnProject(@NonNull AtomicReference<Project> projectReference)
            throws NullPointerException {
        Project project = projectReference.get();
        List<Cell> cellsInProject = project.getCells();
        boolean firstTime = false;
        if (cellsInProject != null) {
            if (!cellsInProject.isEmpty()) {
                project.setCellHeightWaters(null);
                project.setCellStates(null);
                project = repository.saveAndFlush(project);
            } else {
                firstTime = true;
            }
        } else {
            firstTime = true;
        }
        projectReference.set(project);
        if (firstTime) {
            initCellsFirstTime(projectReference);
        }
        initMatrix(projectReference);
    }

    private void initCellsFirstTime(@NonNull AtomicReference<Project> projectReference)
            throws NullPointerException {
        Project project = projectReference.get();
        int numberOfCellX = project.NUMBER_OF_CELL.getFirst();
        int numberOfCellY = project.NUMBER_OF_CELL.getSecond();
        State dryState = stateService.findAndCreateDryState();
        ArrayList<Cell> cells = new ArrayList<>();
        for (int y = 0; y < numberOfCellY; y++) {
            for (int x = 0; x < numberOfCellX; x++) {
                Cell cell = cellService.createNewCellWithAllData(project, dryState, x, y);
                cells.add(cell);
            }
        }
        cells = cellService.updateHeightForCells(cells);
        project.setCells(cells);
        projectReference.set(project);
    }

    private void initMatrix(@NonNull AtomicReference<Project> projectReference)
            throws NullPointerException {
        Project project = projectReference.get();
        int numberOfCellX = project.NUMBER_OF_CELL.getFirst();
        int numberOfCellY = project.NUMBER_OF_CELL.getSecond();
        logger.debug("Number of X: " + numberOfCellX + ", Number Of Y: " + numberOfCellY);
        project.MATRIX = new Cell[numberOfCellY][numberOfCellX];
        List<Cell> cells = project.getCells();
        for (Cell cell: cells) {
            project.MATRIX[cell.getYArray()][cell.getXArray()] = cell;
        }
        projectReference.set(project);
    }
    // End of Region of Method to initiate prediction

    private void runPredictionProcess(@NonNull AtomicReference<Project> projectReference)
            throws NullPointerException {
        Project project = projectReference.get();
        long endTime = project.getEndTime();
        int timeStep = project.getTimeStep() * 60; // in seconds
        logger.debug(project.getName() + " is running");
        for (long timeElapsed = project.getStartTime(); timeElapsed < endTime; timeElapsed = timeElapsed + timeStep) {
            logger.debug("Time elapsed: " + timeElapsed + " from " + endTime);
            logger.debug("Start iterate all cell");
            projectReference.set(project);
            iterateAllCell(projectReference, timeElapsed, timeStep);
            project = projectReference.get();
            logger.debug("End iterate all cell");
            logger.debug("Start process from all active cells : " + project.ACTIVE_CELLS.size() + "cells");
            while (project.ACTIVE_CELLS.size() != 0) {
                Cell activeCell = project.ACTIVE_CELLS.poll();
                project.PROCESSED_CELLS.add(activeCell);
                ArrayList<Cell> processedCells = project.INUNDATION_MODEL.process(projectReference,
                        project.MATRIX[activeCell.getYArray()][activeCell.getXArray()]);
                for (Cell cell: processedCells) {
                    project.MATRIX[cell.getYArray()][cell.getXArray()] = cellService.updateCellByRunOff(projectReference,
                            cell, cell.getWaterHeight(), timeElapsed);
                }
            }
            logger.debug("Ending process from all active cells");
            saveAllCell(projectReference, timeElapsed);
            project.PROCESSED_CELLS = new ArrayList<>();
        }
        project.setDone(true);
        logger.debug(project.getName() + " is finished");
        projectReference.set(project);
    }

    private void iterateAllCell(@NonNull AtomicReference<Project> projectReference, long timeElapsed, int timeStep)
            throws NullPointerException {
        Project project = projectReference.get();
        for (int y = 0; y < project.MATRIX.length; y++) {
            for (int x = 0; x < project.MATRIX[0].length; x++) {
                AtomicReference<Cell> cellReference = new AtomicReference<>(project.MATRIX[y][x]);
                Cell cell = cellReference.get();
                District district = cell.getDistrict();
                List<Weather> weathers = districtService.getDataOfWeatherByDistrictAndTime(district,timeElapsed, timeElapsed + timeStep);
                double runOff = project.SELECTED_MODEL.calculate(project.getVariable(), cellReference, weathers, timeElapsed, timeStep);
                project.MATRIX[y][x] = cellService.updateCellByRunOff(projectReference, project.MATRIX[y][x], runOff, timeElapsed);
            }
        }
        projectReference.set(project);
    }

    private void saveAllCell(@NonNull AtomicReference<Project> projectReference, long timeElapsed)
            throws NullPointerException {
        Project project = projectReference.get();
        for (int y = 0; y < project.MATRIX.length; y++) {
            for (int x = 0; x < project.MATRIX[0].length; x++) {
                Cell cell = project.MATRIX[y][x];
                State state = stateService.findAndCreateDryState();
                if (cell.getWaterHeight() > 0) {
                    state = stateService.findAndCreateWetState();
                }
                project.MATRIX[y][x] = cellService.createOrUpdateCellStateRecord(project, cell, state, timeElapsed);
                project.MATRIX[y][x] = cellService.createOrUpdateHeightWaterRecord(project, cell, timeElapsed);
            }
        }
        projectReference.set(project);
    }

    /* Get from http://stackoverflow.com/questions/1253499/simple-calculations-for-working-with-lat-lon-km-distance */
    private double convertMToLat(double m) {
        return (m / 1000) / 110.574;
    }

    private double convertMToLon(double m, double latInDegree) {
        return (m / 1000) * Math.acos(toRadians(latInDegree)) / 111.320;
    }

    /* Get from http://stackoverflow.com/questions/9705123/how-can-i-get-sin-cos-and-tan-to-use-degrees-instead-of-radians */
    private double toRadians(double angle) {
        return angle * (Math.PI / 180);
    }
}
