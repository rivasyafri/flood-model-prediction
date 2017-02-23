package com.mofp.service.impl;

import com.mofp.dao.ProjectRepository;
import com.mofp.model.*;
import com.mofp.model.data.District;
import com.mofp.model.data.Weather;
import com.mofp.model.data.google.geocoding.GoogleGeocodingResponse;
import com.mofp.model.data.openmap.weather.OpenMapWeatherFiveDayResponse;
import com.mofp.model.moving.CellHeightWater;
import com.mofp.model.moving.CellState;
import com.mofp.service.CellService;
import com.mofp.service.ProjectService;
import com.mofp.service.StateService;
import com.mofp.service.data.DistrictService;
import com.mofp.service.data.GoogleElevationService;
import com.mofp.service.data.GoogleGeocodingService;
import com.mofp.service.data.OpenWeatherMapService;
import com.mofp.service.method.ChenModel;
import com.mofp.service.method.PrasetyaModel;
import com.mofp.service.method.VICModel;
import com.mofp.service.method.formula.Evapotranspiration;
import com.mofp.service.method.formula.WaterBalance;
import com.mofp.service.support.impl.DefaultBaseServiceImpl;
import com.mofp.util.UnitTemperature;
import lombok.NonNull;
import org.apache.log4j.Logger;
import org.geolatte.geom.G2D;
import org.geolatte.geom.LineString;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.codec.Wkt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author rivasyafri
 */
@Service
public class DefaultProjectServiceImpl extends DefaultBaseServiceImpl<ProjectRepository> implements ProjectService {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private GoogleElevationService googleElevationService;

    @Autowired
    private GoogleGeocodingService googleGeocodingService;

    @Autowired
    private OpenWeatherMapService openWeatherMapService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private StateService stateService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CellService cellService;

    private State WET_STATE = null;
    private State DRY_STATE = null;
    private ArrayList<int[][]> NEIGHBOR = null;
    private final int MAX_NUMBER_OF_LOCATION_IN_REQUEST = 50;
    private final Neighborhood NEIGHBORHOOD = new Neighborhood();
    private final Random RANDOM_GENERATOR = new Random();

    @Override
    public Project checkOrRunPredictionProcess(long id)
            throws IllegalAccessException, NullPointerException {
        try {
            Project project = repository.findOne(id);
            if (project.isDone()) {
                return project;
            }
            if (project.getArea() != null) {
                AtomicReference<Project> projectReference = new AtomicReference<>(project);
                initProject(projectReference);
                runPredictionProcess(projectReference);
                project = projectReference.get();
                repository.saveAndFlush(project);
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
    public Polygon createRectangleBound(double northLat, double westLong, double southLat, double eastLong) {
        List<Pair<Double, Double>> points = new ArrayList<>();
        points.add(Pair.of(northLat, eastLong));
        points.add(Pair.of(southLat, eastLong));
        points.add(Pair.of(southLat, westLong));
        points.add(Pair.of(northLat, westLong));
        try {
            Polygon<G2D> polygon = createPolygonWkt(points);
            return polygon;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Polygon createPolygonWkt(@NonNull List<Pair<Double, Double>> points)
            throws IllegalArgumentException, NullPointerException {
        if (points.size() < 3) {
            throw new IllegalArgumentException("List of points is consist of less than 3.");
        } else {
            logger.debug("Create polygon using WKT initiated.");
            StringBuilder wkt = new StringBuilder("SRID=4326;POLYGON((");
            for (int i = 0; i < points.size(); i++) {
                Pair<Double, Double> point = points.get(i);
                wkt.append(point.getSecond() + " " + point.getFirst() + ", ");
            }
            // Close the polygon
            Pair<Double, Double> point = points.get(0);
            wkt.append(point.getSecond() + " " + point.getFirst());
            wkt.append("))");
            return (Polygon<G2D>) Wkt.fromWkt(wkt.toString());
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

    private void initProject(AtomicReference<Project> projectReference) {
        logger.debug("Initiation start");
        initVariable();
        logger.debug("Initiation for global variable from area start");
        setGlobalPairVariablesFromArea(projectReference);
        logger.debug("Initiation for global variable from area end");
        logger.debug("Initiation for flood model start");
        setSelectedFloodModel(projectReference);
        logger.debug("Initiation for flood model end");
        logger.debug("Initiation for matrix cellular automata start");
        initMatrixOnProject(projectReference);
        logger.debug("Initiation for matrix cellular automata end");
        logger.debug("Initiation end");
    }

    private void setSelectedFloodModel(@NonNull AtomicReference<Project> projectReference)
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
        projectReference.set(project);
    }

    private void setGlobalPairVariablesFromArea(@NonNull AtomicReference<Project> projectReference)
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

    private void initMatrixOnProject(@NonNull AtomicReference<Project> projectReference)
            throws NullPointerException {
        Project project = projectReference.get();
        int numberOfCellX = project.NUMBER_OF_CELL.getFirst();
        int numberOfCellY = project.NUMBER_OF_CELL.getSecond();
        logger.debug("Number of X: " + numberOfCellX + ", Number Of Y: " + numberOfCellY);
        project.MATRIX = new Cell[numberOfCellY][numberOfCellX];
        List<Cell> cellsInProject = project.getCells();
        boolean firstTime = false;
        if (cellsInProject != null) {
            if (!cellsInProject.isEmpty()) {
                project.setCellHeightWaters(null);
                project.setCellStates(null);
                repository.saveAndFlush(project);
                for (Cell cell: cellsInProject) {
                    int x = cell.getXArray();
                    int y = cell.getYArray();
                    project.MATRIX[y][x] = cell;
                }
            } else {
                firstTime = true;
            }
        } else {
            firstTime = true;
        }
        projectReference.set(project);
        if (firstTime) {
            initMatrixFirstTime(projectReference);
        }
    }

    private void initMatrixFirstTime(@NonNull AtomicReference<Project> projectReference) {
        Project project = projectReference.get();
        int numberOfCellX = project.NUMBER_OF_CELL.getFirst();
        int numberOfCellY = project.NUMBER_OF_CELL.getSecond();
        double latNorth = project.NORTH_WEST.getFirst();
        double longWest = project.NORTH_WEST.getSecond();
        double deltaX = project.DELTA.getFirst();
        double deltaY = project.DELTA.getSecond();
        ArrayList<ArrayList<Cell>> listOfCellForGoogleElevation = new ArrayList<>();
        ArrayList<Cell> cellsForGoogleElevation = new ArrayList<>();
        for (int y = 0; y < numberOfCellY; y++) {
            for (int x = 0; x < numberOfCellX; x++) {
                Cell cell = new Cell(x, y, DRY_STATE);
                cell.setArea(projectService.createRectangleBound(
                        latNorth + deltaY * y,
                        longWest + deltaX * x,
                        latNorth + deltaY * (y + 1),
                        longWest + deltaX * (x + 1)
                ));
                cell.setProject(project);
                cell.randomizeData(); // for experiment
                GoogleGeocodingResponse geocodingResponse = googleGeocodingService.getGoogleGeocodingResponse(cell);
                District district = districtService.findOneOrCreateNewDistrict(geocodingResponse);
                if (district != null) {
                    List<Weather> weathers = district.getWeathers();
                    if (weathers == null) {
                        if (weathers.isEmpty()) {
                            OpenMapWeatherFiveDayResponse openMapResponse = openWeatherMapService.getOpenWeatherMapResponse(district);
                            district = districtService.findOneAndAddNewMovingObjectDataDistrict(openMapResponse, district);
                        }
                    }
                    cell.setDistrict(district);
                }
                cell = cellService.getRepository().saveAndFlush(cell);
                logger.debug(cell.toString());
                project.MATRIX[y][x] = cell;
                if (cellsForGoogleElevation.size() < MAX_NUMBER_OF_LOCATION_IN_REQUEST) {
                    cellsForGoogleElevation.add(cell);
                } else {
                    listOfCellForGoogleElevation.add(cellsForGoogleElevation);
                    cellsForGoogleElevation = new ArrayList<>();
                    cellsForGoogleElevation.add(cell);
                }
            }
        }
        listOfCellForGoogleElevation.add(cellsForGoogleElevation);
        ArrayList<Cell> cells = googleElevationService.getElevationForAllCells(listOfCellForGoogleElevation);
        cellService.getRepository().save(cells);
        cellService.getRepository().flush();
        projectReference.set(project);
    }

    private void runPredictionProcess(@NonNull AtomicReference<Project> projectReference)
            throws NullPointerException {
        Project project = projectReference.get();
        long endTime = project.getEndTime();
        int timeStep = project.getTimeStep() * 60;
        logger.debug(project.getName() + " is running");
        for (long timeElapsed = project.getStartTime(); timeElapsed < endTime; timeElapsed = timeElapsed + timeStep) {
            logger.debug("Time elapsed: " + timeElapsed + " from " + endTime);
            logger.debug("Start inundation from all active cells : " + project.ACTIVE_CELLS.size() + "cells");
            while (project.ACTIVE_CELLS.size() != 0) {
                Cell activeCell = project.ACTIVE_CELLS.poll();
                project.PROCESSED_CELLS.add(activeCell);
                project.MATRIX[activeCell.getYArray()][activeCell.getXArray()] =
                        inundation(projectReference, project.MATRIX[activeCell.getYArray()][activeCell.getXArray()]);
            }
            logger.debug("Ending inundation from all active cells : " + project.ACTIVE_CELLS.size() + "cells");
            logger.debug("Start iterate all cell");
            projectReference.set(project);
            iterateAllCell(projectReference, timeElapsed, timeStep);
            project = projectReference.get();
            logger.debug("End iterate all cell");
            project.ACTIVE_CELLS = project.NEW_ACTIVE_CELLS;
            project.PROCESSED_CELLS = new ArrayList<>();
            project.NEW_ACTIVE_CELLS = new PriorityQueue<>();
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
                Cell cell = project.MATRIX[y][x];
                List<Weather> weathers = getWeatherData(cell, timeElapsed, timeStep);
                long time = getDeltaTime(cell, timeElapsed, timeStep);
                double precipitation = getPrecipitationMinusEvapotranspiration(weathers, cell.getHeight(),
                        project.getVariable(), timeElapsed);
                double runOff = project.SELECTED_MODEL.calculateRunOff(project.getVariable(), project.MATRIX[y][x],
                        precipitation, time);
                double waterBalance = WaterBalance.calculate(project.MATRIX[y][x].getWaterBalanceBefore(),
                        precipitation, runOff, timeElapsed);
                project.MATRIX[y][x].setWaterBalanceAfter(waterBalance);
                project.MATRIX[y][x] = updateCellByRunOff(projectReference, project.MATRIX[y][x], runOff, timeElapsed);
            }
        }
        projectReference.set(project);
    }

    private Cell inundation(@NonNull AtomicReference<Project> projectReference, Cell cell)
            throws NullPointerException {
        Project project = projectReference.get();
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
        double totalHeight = cell.getTotalHeight();
        for (Cell neighborCell : neighborCells) {
            totalHeight += neighborCell.getTotalHeight();
        }
        double averageOfTotalHeight = totalHeight / (neighborCells.size() + 1);

        ArrayList<AtomicReference<Cell>> processedNeighborCells = new ArrayList<>();
        for (Cell neighborCell : neighborCells) {
            if (neighborCell.getTotalHeight() < averageOfTotalHeight &&
                    neighborCell.getTotalHeight() < cell.getTotalHeight()) {
                AtomicReference<Cell> processedCell = new AtomicReference<>(
                        project.MATRIX[neighborCell.getYArray()][neighborCell.getXArray()]);
                processedNeighborCells.add(processedCell);
            }
        }

        double inundation = cell.getWaterHeight() / (processedNeighborCells.size() + 1);
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
            processedNeighborCell.updateTotalHeight();
            processedNeighborCellReference.set(processedNeighborCell);
            if (!project.PROCESSED_CELLS.contains(processedNeighborCell)) {
                project.ACTIVE_CELLS.add(processedNeighborCell);
            }
        }
        cell.setWaterHeight(inundation + deltaCenter);
        cell.updateTotalHeight();
        projectReference.set(project);
        return cell;
    }

    private Cell updateCellByRunOff(@NonNull AtomicReference<Project> projectReference, Cell beforeCell,
                                    double runOff, long timeElapsed) throws NullPointerException {
        Project project = projectReference.get();
        Cell cell = beforeCell;
        if (runOff > 0) {
            cell.setWaterHeight(runOff);
            cell.updateTotalHeight();
            if (cell.getTimeStartFlooded() == 0) {
                cell.setCurrentState(WET_STATE);
                cell.setTimeStartFlooded(timeElapsed);
                cell = cellService.createOrUpdateCellStateRecord(project, cell, WET_STATE, timeElapsed);
            }
            cell = cellService.createOrUpdateHeightWaterRecord(project, cell, timeElapsed);
            project.NEW_ACTIVE_CELLS.add(cell);
        } else if (cell.getWaterHeight() > 0) {
            cell.setWaterHeight(runOff);
            cell.updateTotalHeight();
            cell.setCurrentState(DRY_STATE);
            cell.setTimeStartFlooded(0);
            cell = cellService.createOrUpdateCellStateRecord(project, cell, DRY_STATE, timeElapsed);
        }
        return cell;

    }

    private long getDeltaTime(@NonNull Cell cell, long timeElapsed, int timeStep) throws NullPointerException {
        long time = 0;
        if (cell.getTimeStartFlooded() != 0) {
            time = timeElapsed - cell.getTimeStartFlooded();
        }
        time += timeStep;
        return time;
    }

    protected double getPrecipitationMinusEvapotranspiration(List<Weather> weathers, double height, Variable variable, long startTime) {
        double evapotranspiration = 0;
        double rain = 0;
        boolean usingEvapotranspiration = variable.isUsingEvapotranspiration();
        boolean evapotranspirationByVariableFromUser = variable.isEvapotranspirationByData();
        if (weathers != null) {
            for (Weather weather: weathers) {
                Double rainDouble = weather.getRain();
                if (rainDouble != null) {
                    long startTimeData = weather.getStartTime();
                    long endTimeData = weather.getEndTime();
                    long intervalData = endTimeData - startTimeData;
                    long intervalNeeded = intervalData - (startTime - startTimeData);
                    rain += rainDouble * (intervalNeeded / intervalData);
                }
                if (usingEvapotranspiration && !evapotranspirationByVariableFromUser) {
                    Double radiation = weather.getRadiation();
                    Double geothermal = weather.getGeothermal();
                    Double maxTemperature = weather.getMaxTemperature();
                    Double minTemperature = weather.getMinTemperature();
                    Double humidity = weather.getRelativeHumidity();
                    Double windSpeed = weather.getWindSpeed();
                    Double waterVapor = weather.getWaterVapor();
                    if (radiation != null && geothermal != null && maxTemperature != null && minTemperature != null &&
                            humidity != null && windSpeed != null && waterVapor!= null ) {
                        long startTimeData = weather.getStartTime();
                        long endTimeData = weather.getEndTime();
                        long intervalData = endTimeData - startTimeData;
                        double hour = intervalData / (3600);
                        double cn = 37 * hour; // still hardcode, use short plant 3 is hour
                        double cd = 0.24 * hour; // still hardcode, use short plant. 3 is hour
                        double calculationResult = Evapotranspiration.calculateWithRhMean(radiation, geothermal, cn, cd, windSpeed,
                                minTemperature, maxTemperature, UnitTemperature.CELCIUS, height, humidity);
                        long intervalNeeded = intervalData - (startTime - startTimeData);
                        evapotranspiration += calculationResult * (intervalNeeded / intervalData);
                    }
                }
            }
        }
        return rain - evapotranspiration;
    }

    private List<Weather> getWeatherData(@NonNull Cell cell, long startTime, int timeStep) {
        District district = cell.getDistrict();
        if (district == null) {
            return null;
        } else {
            return districtService.getDataOfWeatherByDistrictAndTime(district, startTime, startTime + timeStep);
        }
    }

    private void initVariable() {
        if (WET_STATE == null) {
            WET_STATE = stateService.findAndCreateWetState();
        }
        if (DRY_STATE == null) {
            DRY_STATE = stateService.findAndCreateDryState();
        }
        if (NEIGHBOR == null) {
            NEIGHBOR = NEIGHBORHOOD.moore();
        }
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
