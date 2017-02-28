package com.mofp.service.data.impl;

import com.mofp.model.data.District;
import com.mofp.model.data.openmap.weather.OpenMapWeatherFiveDayResponse;
import com.mofp.service.data.OpenWeatherMapService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author rivasyafri
 */
@Service
public class DefaultOpenWeatherMapServiceImpl implements OpenWeatherMapService {

    private final Logger logger = Logger.getLogger(getClass());

    private String key = "7a0a9259cd133642bb1e54c5403a0fe1";

    @Override
    public OpenMapWeatherFiveDayResponse getOpenWeatherMapResponse(District district) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://api.openweathermap.org/data/2.5/forecast?q=" +
                district.toString() + "&mode=json&appid=" + key;
        logger.debug("Getting data from " + url);
        OpenMapWeatherFiveDayResponse response = restTemplate.getForObject(url, OpenMapWeatherFiveDayResponse.class);
        return response;
    }
}
