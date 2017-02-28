package com.mofp.service.data;

import com.mofp.model.data.District;
import com.mofp.model.data.openmap.weather.OpenMapWeatherFiveDayResponse;

/**
 * @author rivasyafri
 */
public interface OpenWeatherMapService {

    OpenMapWeatherFiveDayResponse getOpenWeatherMapResponse(District district);
}
