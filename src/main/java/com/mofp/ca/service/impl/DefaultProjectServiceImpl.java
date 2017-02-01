package com.mofp.ca.service.impl;

import com.mofp.ca.dao.ProjectRepository;
import com.mofp.ca.dao.StateRepository;
import com.mofp.ca.model.Cell;
import com.mofp.ca.model.GlobalFloodPrediction;
import com.mofp.ca.model.Project;
import com.mofp.ca.service.ProjectService;
import com.mofp.flood.prediction.HortonEquation;
import com.mofp.flood.prediction.PrasetyaModel;
import org.apache.log4j.Logger;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.codec.Wkt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * @author rivasyafri
 */
@Service
public class DefaultProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private StateRepository stateRepository;

    private final Logger logger = Logger.getLogger(getClass());

    private GlobalFloodPrediction GLOBAL;

    @Override
    public Project run(long id) {
        try {
            Project project = projectRepository.findOne(id);
//            if (PROJECT.isDone() || PROJECT.getCellList().size() != 0) {
            if (project.getCellList().size() != 0 && project.getArea() != null) {
                return project;
            } else {
//                ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//                ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context.getBean("taskExecutor");
//                if (taskExecutor.getActiveCount() == 0) {
//                    PredictionThread predictionThread = new PredictionThread();
//                    predictionThread.setName("PROJECT id -" + PROJECT.getId() +
//                            "- with name '"+ PROJECT.getName() +"'");
//                    predictionThread.setProject(PROJECT);
//                    taskExecutor.execute(predictionThread);
//                } else {
//                    logger.debug("There is another instance thread still running.");
//                }
                logger.debug(project.getName() + " is running");
                GLOBAL = new GlobalFloodPrediction(project);
                initState();
                this.runProject();
                logger.debug(project.getName() + " is finished");
                project.setDone(true);
                projectRepository.saveAndFlush(project);
                return project;
            }
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }
    }

    @Override
    public Polygon createRectangleFromBounds(double northLat, double westLong,
                                             double southLat, double eastLong) {
        logger.debug("Create polygon using WKT initiated");
        StringBuilder wkt = new StringBuilder("SRID=4326;POLYGON((");
        wkt.append(northLat + " " + eastLong + ", ");
        wkt.append(southLat + " " + eastLong + ", ");
        wkt.append(southLat + " " + westLong + ", ");
        wkt.append(northLat + " " + westLong + ", ");
        wkt.append(northLat + " " + eastLong);
        wkt.append("))");
        return (Polygon<G2D>) Wkt.fromWkt(wkt.toString());
    }

    private Project runProject() {
        for (int timeStep = 0; timeStep < GLOBAL.INTERVAL; timeStep = timeStep + GLOBAL.TIME_STEP) {
            while (GLOBAL.ACTIVE_CELLS.size() != 0) {
                Cell activeCell = GLOBAL.ACTIVE_CELLS.poll();
                runInundationModel(activeCell);
            }
            calculateRunOffForAllCells("", timeStep);
            GLOBAL.ACTIVE_CELLS = GLOBAL.NEW_ACTIVE_CELLS;
            GLOBAL.NEW_ACTIVE_CELLS = new PriorityQueue<>();
        }
        return GLOBAL.PROJECT;
    }
    private void initState() {
        GLOBAL.WET_STATE = stateRepository.findByName("WET").get(0);
        GLOBAL.DRY_STATE = stateRepository.findByName("DRY").get(0);
    }
    private void calculateRunOffForAllCells(String model, int timeStep) {
        PrasetyaModel chosenModel = new PrasetyaModel();
        for (Cell[] cells : GLOBAL.MATRIX) {
            for (Cell cell : cells) {
                double runOff = cell.getWaterHeight();

                // Add variation of infiltration capacity
                int time = timeStep - cell.getTimeStartFlooded();
                if (cell.getTimeStartFlooded() == 0) {
                    time = 0;
                }
                double infiltrationRate = HortonEquation.calculate(cell.getConstantInfiltrationCapacity(),
                        cell.getInitialInfiltrationCapacity(), cell.getKValue(), time);

                // Calculation of water balance and saving, can add drainage
                double waterBalance = infiltrationRate;
                double saving = 0;
                double temp = infiltrationRate - runOff;
                if (temp > 0) {
                    waterBalance = runOff;
                    saving = infiltrationRate - runOff;
                }
                cell.setWaterBalance(cell.getWaterBalance() + waterBalance);

                // Add precipitation of cell
                double precipitation = GLOBAL.RANDOM_GENERATOR.nextDouble();

                // Calculation of run off
                runOff = chosenModel.calculateRunOff(precipitation, saving) + (runOff - waterBalance);

                // Set state, can add process to save the state
                if (runOff > 0) {
                    cell.setWaterHeight(runOff);
                    cell.updateTotalHeight();
                    if (cell.getTimeStartFlooded() > 0) {
                        cell.setCurrentState(GLOBAL.WET_STATE);
                        cell.setTimeStartFlooded(timeStep);
                    }
                    GLOBAL.NEW_ACTIVE_CELLS.add(cell);
                } else if (cell.getWaterHeight() > 0) {
                    cell.setWaterHeight(runOff);
                    cell.updateTotalHeight();
                    cell.setCurrentState(GLOBAL.DRY_STATE);
                    cell.setTimeStartFlooded(0);
                }
            }
        }
    }
    private void runInundationModel(Cell cell) {
        int _x, _y;
        ArrayList<Cell> neighborCells = new ArrayList<>();
        for (int[][] i : GLOBAL.NEIGHBOR) {
            for (int[] j : i) {
                _x = j[0] + cell.getXArray();
                _y = j[1] + cell.getYArray();
                if (_x > 0 && _y > 0) {
                    neighborCells.add(GLOBAL.MATRIX[_y][_x]);
                }
            }
        }
        double totalHeight = cell.getTotalHeight();
        for (Cell neighborCell : neighborCells) {
            totalHeight += neighborCell.getTotalHeight();
        }
        double averageOfTotalHeight = totalHeight / (neighborCells.size() + 1);

        ArrayList<Cell> processedNeighborCells = new ArrayList<>();
        for (Cell neighborCell: neighborCells) {
            if (neighborCell.getTotalHeight() < averageOfTotalHeight &&
                    neighborCell.getTotalHeight() < cell.getTotalHeight()) {
                processedNeighborCells.add(neighborCell);
            }
        }

        double inundation = cell.getWaterHeight() / (processedNeighborCells.size() + 1);
        double deltaCenter = 0;
        for (Cell processedNeighborCell: processedNeighborCells) {
            if (processedNeighborCell.getTotalHeight() + inundation > cell.getTotalHeight()) {
                double delta = cell.getTotalHeight() - processedNeighborCell.getTotalHeight();
                processedNeighborCell.setWaterHeight(processedNeighborCell.getWaterHeight() + delta);
                deltaCenter += inundation - delta;
            } else {
                processedNeighborCell.setWaterHeight(processedNeighborCell.getWaterHeight() + inundation);
            }
            if (!GLOBAL.ACTIVE_CELLS.contains(processedNeighborCell)) {
                GLOBAL.ACTIVE_CELLS.add(processedNeighborCell);
            }
            processedNeighborCell.updateTotalHeight();
        }
        cell.setWaterHeight(inundation + deltaCenter);
        cell.updateTotalHeight();
    }
}
