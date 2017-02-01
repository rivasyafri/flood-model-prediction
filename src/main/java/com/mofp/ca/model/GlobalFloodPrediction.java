package com.mofp.ca.model;

import org.geolatte.geom.G2D;
import org.geolatte.geom.LineString;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * @author rivasyafri
 */
public class GlobalFloodPrediction {

    ////*********************** Model Properties ******************************** ////
    public double SIZE_X;
    public double SIZE_Y;
    public double DELTA_X;
    public double DELTA_Y;
    public int TIME_STEP;
    public int INTERVAL;

    //// ************************** Neighborhood *************************************** ////
    public Neighborhood NEIGHBORHOOD;
    public ArrayList<int[][]> NEIGHBOR;

    //// ************************** Public Variables *************************************** ////
    public Project PROJECT;
    public Cell[][] MATRIX;
    public PriorityQueue<Cell> ACTIVE_CELLS;
    public PriorityQueue<Cell> NEW_ACTIVE_CELLS;
    public Random RANDOM_GENERATOR;

    //// ************************** Constant State *************************************** ////
    public State WET_STATE;
    public State DRY_STATE;

    public GlobalFloodPrediction() {
        TIME_STEP = 1001;
        INTERVAL = 100000;
        SIZE_X = 800;
        SIZE_Y = 800;
        initVariables();
        initArea();
        initMatrix();
    }
    public GlobalFloodPrediction(Project PROJECT) {
        this.PROJECT = PROJECT;
        TIME_STEP = PROJECT.getTimeStep();
        INTERVAL = PROJECT.getTimeStep();
        initVariables();
        initArea();
        initMatrix();
    }
    private void initVariables() {
        NEIGHBORHOOD = new Neighborhood();
        NEIGHBOR = NEIGHBORHOOD.use("moore");

        ACTIVE_CELLS = new PriorityQueue<>();
        NEW_ACTIVE_CELLS = new PriorityQueue<>();
        RANDOM_GENERATOR = new Random();
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
        SIZE_X = Math.abs(lon1 - lon2);
        SIZE_Y = Math.abs(lat1 - lat2);
        DELTA_X = PROJECT != null ? convertMToLat(PROJECT.getCellSize()) : convertMToLat(1000);
        DELTA_Y = PROJECT != null ? convertMToLon(PROJECT.getCellSize(), lon1) : convertMToLon(1000, lon1);
    }
    private void initMatrix() {
        Long gridX = Math.round(SIZE_X / DELTA_X);
        Long gridY = Math.round(SIZE_Y / DELTA_Y);
        int numberOfCellX = gridX.intValue();
        int numberOfCellY = gridY.intValue();
        MATRIX = new Cell[numberOfCellY][numberOfCellX];
        List<Cell> cells = new ArrayList<>();
        for (int y = 0; y < numberOfCellY; y++) {
            for (int x = 0; x < numberOfCellX; x++) {
                Cell cell = new Cell(x, y, DRY_STATE);
                MATRIX[y][x] = cell;
                cells.add(cell);
                MATRIX[y][x].randomizeData(); // for experiment
            }
        }
        PROJECT.setCellList(cells);
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
