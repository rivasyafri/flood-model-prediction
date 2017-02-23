package com.mofp.service.data.impl;

import com.mofp.model.Cell;
import com.mofp.model.data.google.elevation.GoogleElevation;
import com.mofp.model.data.google.elevation.GoogleElevationResponse;
import com.mofp.service.data.GoogleElevationService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author rivasyafri
 */
@Service
public class DefaultGoogleElevationServiceImpl implements GoogleElevationService {

    private final Logger logger = Logger.getLogger(getClass());

    private String key = "AIzaSyCA6AY3nH7zkkYlvSWj3t_eXKBCbyQmtGs";

    @Override
    public ArrayList<Cell> getElevationForAllCells(ArrayList<ArrayList<Cell>> listOfCellForGoogleElevation) {
        logger.debug("Start getting elevation from google elevation");
        ArrayList<Cell> allCells = new ArrayList<>();
        for (ArrayList<Cell> cellsForGoogleElevation : listOfCellForGoogleElevation) {
            String combinedCellForGoogleElevation = createStringCombinedCellForGoogleElevation(cellsForGoogleElevation);
            GoogleElevationResponse response = getFromGoogleElevation(combinedCellForGoogleElevation);
            if (response.getStatus().compareTo("OK") != 0) {
                logger.debug("response not OK : " + response.getResults().size());
            }
            ArrayList<GoogleElevation> elevations = new ArrayList<>(response.getResults());
            for (int i = 0; i < cellsForGoogleElevation.size(); i++) {
                Cell cell = cellsForGoogleElevation.get(i);
                GoogleElevation elevation = elevations.get(i);
                cell.setHeight(elevation.getElevation());
                cell.updateTotalHeight();
                allCells.add(cell);
            }
        }
        logger.debug("End getting elevation from google elevation");
        return allCells;
    }

    @Override
    public Cell getHeightFromGoogleElevation(Cell cell) {
        StringBuilder stringBuilder = new StringBuilder("locations=");
        HashMap<Integer, Double> map = cell.getCenterPointOfArea();
        stringBuilder.append(map.get(1));
        stringBuilder.append(",");
        stringBuilder.append(map.get(2));
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://maps.googleapis.com/maps/api/elevation/json?"
                + stringBuilder.toString() + "&key=AIzaSyCA6AY3nH7zkkYlvSWj3t_eXKBCbyQmtGs";
        logger.debug("Getting data from " + url);
        GoogleElevationResponse response = restTemplate.getForObject(url, GoogleElevationResponse.class);
        ArrayList<GoogleElevation> elevations = new ArrayList<>(response.getResults());
        GoogleElevation elevation = elevations.get(0);
        cell.setHeight(elevation.getElevation());
        cell.updateTotalHeight();
        return cell;
    }

    private GoogleElevationResponse getFromGoogleElevation(String combinedCellForGoogleElevation) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://maps.googleapis.com/maps/api/elevation/json?"
                + combinedCellForGoogleElevation + "&key=" + key;
        logger.debug("Getting data from " + url);
        GoogleElevationResponse response = restTemplate.getForObject(url, GoogleElevationResponse.class);
        return response;
    }

    private String createStringCombinedCellForGoogleElevation(ArrayList<Cell> cellsForGoogleElevation) {
        StringBuilder stringBuilder = new StringBuilder("locations=");
        for (int i = 0; i < cellsForGoogleElevation.size(); i++) {
            Cell cell = cellsForGoogleElevation.get(i);
            HashMap<Integer, Double> map = cell.getCenterPointOfArea();
            stringBuilder.append(map.get(1));
            stringBuilder.append(",");
            stringBuilder.append(map.get(2));
            if (i < cellsForGoogleElevation.size() - 1) {
                stringBuilder.append("|");
            }
        }
        return stringBuilder.toString();
    }
}
