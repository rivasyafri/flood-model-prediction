package com.mofp.service.data;

import com.mofp.model.Cell;

import java.util.ArrayList;

/**
 * @author rivasyafri
 */
public interface GoogleElevationService {

    ArrayList<Cell> getElevationForAllCells(ArrayList<ArrayList<Cell>> listOfCellForGoogleElevation);
    Cell getHeightFromGoogleElevation(Cell cell);
}
