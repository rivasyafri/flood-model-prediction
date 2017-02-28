package com.mofp.service.data;

import com.mofp.dao.data.DistrictRepository;
import com.mofp.model.data.District;
import com.mofp.model.data.Weather;
import com.mofp.model.data.google.geocoding.GoogleGeocodingResponse;
import com.mofp.model.data.openmap.weather.OpenMapWeatherFiveDayResponse;
import com.mofp.service.support.AbstractBaseService;
import lombok.NonNull;

import java.util.List;

/**
 * @author rivasyafri
 */
public interface DistrictService extends AbstractBaseService<DistrictRepository> {

    /**
     * Find or create new district from google geocoding response
     * @param latitude latitude of district
     * @param longitude longitude of district
     * @return district in db
     */
    District findOneOrCreateNewDistrict(double latitude, double longitude);

    /**
     * Find or create new district from google geocoding response
     * @param response response from google geocoding
     * @return district in db
     */
    District findOneOrCreateNewDistrict(@NonNull GoogleGeocodingResponse response);

    /**
     * Find and create moving object data in district
     * @param district district for open map weather
     * @return district in db
     */
    District findOneAndAddNewMovingObjectDataDistrict(@NonNull District district);

    /**
     * Find and create moving object data in district
     * @param response response from open map weather
     * @return district in db
     */
    District findOneAndAddNewMovingObjectDataDistrict(@NonNull OpenMapWeatherFiveDayResponse response,
                                                      @NonNull District district);

    /**
     * Find, create, and update data in district, also with the moving object weather data
     * @param latitude latitude of district
     * @param longitude longitude of district
     * @return district in db
     */
    District pullDataOfDistrict(double latitude, double longitude);

    /**
     * Find weather data from district based on start time and end time
     * @param district
     * @param startTime
     * @param endTime
     * @return list of weather
     */
    List<Weather> getDataOfWeatherByDistrictAndTime(@NonNull District district, Long startTime, Long endTime);
}
