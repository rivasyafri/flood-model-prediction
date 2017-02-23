package com.mofp.service.data;

import com.mofp.model.Cell;
import com.mofp.model.data.google.geocoding.GoogleGeocodingResponse;

/**
 * @author rivasyafri
 */
public interface GoogleGeocodingService {

    GoogleGeocodingResponse getGoogleGeocodingResponse(Cell cell);
    GoogleGeocodingResponse getGoogleGeocodingResponse(double latitude, double longitude);
}
