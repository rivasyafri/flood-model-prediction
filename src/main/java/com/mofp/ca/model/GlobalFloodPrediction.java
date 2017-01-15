package com.mofp.ca.model;

import com.mofp.ca.dao.StateRepository;
import com.mofp.flood.prediction.HortonEquation;
import com.mofp.flood.prediction.PrasetyaModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author rivasyafri
 */
public class GlobalFloodPrediction {

    @Autowired
    private StateRepository stateRepository;

    ////*********************** Model Properties ******************************** ////
    public int SIZE_X;
    public int SIZE_Y;
    public int CELL_SIZE;
    public int TIME_STEP;
    public int INTERVAL;

    //// ************************** Neighborhood *************************************** ////
    public Neighborhood NEIGHBORHOOD;
    public ArrayList<int[][]> NEIGHBOR;

    //// ************************** Public Variables *************************************** ////
    public Cell[][] MATRIX;
    public ArrayList<Cell> ACTIVE_CELLS;
    public ArrayList<Cell> NEW_ACTIVE_CELLS;
    public Random RANDOM_GENERATOR;

    //// ************************** Constant State *************************************** ////
    private final State WET_STATE = stateRepository.findByName("WET").get(0);
    private final State DRY_STATE = stateRepository.findByName("DRY").get(0);


    public GlobalFloodPrediction() {
        CELL_SIZE = 4;
        TIME_STEP = 1001;
        INTERVAL = 100000;

        NEIGHBORHOOD = new Neighborhood();
        NEIGHBOR = NEIGHBORHOOD.use("moore");

        // Calculation how many cells in here
        ACTIVE_CELLS = new ArrayList<>();
        NEW_ACTIVE_CELLS = new ArrayList<>();
        RANDOM_GENERATOR = new Random();
    }

    public void initialize() {
        int GridX = SIZE_X / CELL_SIZE;
        int GridY = SIZE_Y / CELL_SIZE;
        MATRIX = new Cell[GridX][GridY];
        for (int y = 0; y < GridY; y++) {
            for (int x = 0; x < GridX; x++) {
                MATRIX[y][x] = new Cell(x, y, DRY_STATE);
            }
        }
    }

    public void run() {
        for (int timeStep = 0; timeStep < INTERVAL; timeStep = timeStep + TIME_STEP) {
            calculateRunOffForAllCells("", timeStep);
            for (Cell activeCell: ACTIVE_CELLS) {
                runInundationModel(activeCell);
                ACTIVE_CELLS.remove(activeCell);
            }
            ACTIVE_CELLS = NEW_ACTIVE_CELLS;
            NEW_ACTIVE_CELLS = new ArrayList<>();
            for (Cell activeCell: ACTIVE_CELLS) {
                runInundationModel(activeCell);
                ACTIVE_CELLS.remove(activeCell);
            }
            ACTIVE_CELLS = NEW_ACTIVE_CELLS;
            NEW_ACTIVE_CELLS = new ArrayList<>();
        }
    }

    public void calculateRunOffForAllCells(String model, int timeStep) {
        PrasetyaModel chosenModel = new PrasetyaModel();
        for (Cell[] cells : MATRIX) {
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
                double precipitation = RANDOM_GENERATOR.nextDouble();

                // Calculation of run off
                runOff = chosenModel.calculateRunOff(precipitation, saving) + (runOff - waterBalance);

                // Set state, can add process to save the state
                if (runOff > 0) {
                    cell.setWaterHeight(runOff);
                    if (cell.getTimeStartFlooded() > 0) {
                        cell.setCurrentState(WET_STATE);
                        cell.setTimeStartFlooded(timeStep);
                    }
                    NEW_ACTIVE_CELLS.add(cell);
                } else if (cell.getWaterHeight() > 0) {
                    cell.setWaterHeight(runOff);
                    cell.setCurrentState(DRY_STATE);
                    cell.setTimeStartFlooded(0);
                }
            }
        }
    }

    public void runInundationModel(Cell cell) {
        int _x, _y;
        for (int[][] i : NEIGHBOR) {
            for (int[] j : i) {
                _x = j[0] + cell.getXArray();
                _y = j[1] + cell.getYArray();
                inundationModel(_x, _y);
            }
        }
        inundationModel(cell.getXArray(), cell.getYArray());
    }

    private void inundationModel(int dx, int dy) {

    }
}
