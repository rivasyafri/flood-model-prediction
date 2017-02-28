package com.mofp.service.data;

import com.mofp.model.Cell;
import com.mofp.model.data.google.elevation.GoogleElevationResponse;

import java.util.ArrayList;

/**
 * @author rivasyafri
 */
public interface GoogleElevationService {

    ArrayList<Cell> getElevationForAllCells(ArrayList<Cell> listOfCellForGoogleElevation);
    Cell getHeightFromGoogleElevation(Cell cell);
    GoogleElevationResponse getHeightFromGoogleElevation(double latitude, double longitude);
}
