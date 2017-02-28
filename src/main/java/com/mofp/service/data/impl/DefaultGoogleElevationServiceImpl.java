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
import java.util.List;

/**
 * @author rivasyafri
 */
@Service
public class DefaultGoogleElevationServiceImpl implements GoogleElevationService {

    private final Logger logger = Logger.getLogger(getClass());

    private String key = "AIzaSyCA6AY3nH7zkkYlvSWj3t_eXKBCbyQmtGs";

    private final int MAX_NUMBER_OF_LOCATION_IN_REQUEST = 50;

    @Override
    public ArrayList<Cell> getElevationForAllCells(ArrayList<Cell> listOfCellForGoogleElevation) {
        logger.debug("Start getting elevation from google elevation");
        List<List<Cell>> cells = chopped(listOfCellForGoogleElevation, MAX_NUMBER_OF_LOCATION_IN_REQUEST);
        ArrayList<Cell> allCells = new ArrayList<>();
        for (List<Cell> cellsForGoogleElevation : cells) {
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
        HashMap<Integer, Double> map = cell.getCenterPointOfArea();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(map.get(1));
        stringBuilder.append(",");
        stringBuilder.append(map.get(2));
        GoogleElevationResponse response = getFromGoogleElevation(stringBuilder.toString());
        ArrayList<GoogleElevation> elevations = new ArrayList<>(response.getResults());
        GoogleElevation elevation = elevations.get(0);
        cell.setHeight(elevation.getElevation());
        cell.updateTotalHeight();
        return cell;
    }

    @Override
    public GoogleElevationResponse getHeightFromGoogleElevation(double latitude, double longitude) {
        String combinedCellForGoogleElevation = latitude + "," + longitude;
        return getFromGoogleElevation(combinedCellForGoogleElevation);
    }

    private GoogleElevationResponse getFromGoogleElevation(String combinedCellForGoogleElevation) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://maps.googleapis.com/maps/api/elevation/json?locations="
                + combinedCellForGoogleElevation + "&key=" + key;
        logger.debug("Getting data from " + url);
        GoogleElevationResponse response = restTemplate.getForObject(url, GoogleElevationResponse.class);
        return response;
    }

    private String createStringCombinedCellForGoogleElevation(List<Cell> cellsForGoogleElevation) {
        StringBuilder stringBuilder = new StringBuilder();
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

    private List<List<Cell>> chopped(ArrayList<Cell> list, final int L) {
        List<List<Cell>> parts = new ArrayList<>();
        final int N = list.size();
        for (int i = 0; i < N; i += L) {
            parts.add(new ArrayList<>(
                    list.subList(i, Math.min(N, i + L)))
            );
        }
        return parts;
    }
}
