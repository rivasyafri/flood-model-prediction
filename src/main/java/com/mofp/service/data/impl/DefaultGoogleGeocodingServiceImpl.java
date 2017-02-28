package com.mofp.service.data.impl;

import com.mofp.model.Cell;
import com.mofp.model.data.google.geocoding.GoogleGeocodingResponse;
import com.mofp.service.data.GoogleGeocodingService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * @author rivasyafri
 */
@Service
public class DefaultGoogleGeocodingServiceImpl implements GoogleGeocodingService {

    private final Logger logger = Logger.getLogger(getClass());

    private String key = "AIzaSyCA6AY3nH7zkkYlvSWj3t_eXKBCbyQmtGs";

    @Override
    public GoogleGeocodingResponse getGoogleGeocodingResponse(Cell cell) {
        HashMap<Integer, Double> map = cell.getCenterPointOfArea();
        return getGoogleGeocodingResponse(map.get(1), map.get(2));
    }

    @Override
    public GoogleGeocodingResponse getGoogleGeocodingResponse(double latitude, double longitude) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(latitude);
        stringBuilder.append(",");
        stringBuilder.append(longitude);
        GoogleGeocodingResponse response = getGoogleGeocodingResponse(stringBuilder.toString());
        return response;
    }

    private GoogleGeocodingResponse getGoogleGeocodingResponse(String param) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                + param + "&result_type=political&key=" + key;
        logger.debug("Getting data from " + url);
        GoogleGeocodingResponse response = restTemplate.getForObject(url, GoogleGeocodingResponse.class);
        return response;
    }
}
