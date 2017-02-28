package com.mofp.service.data.impl;

import com.mofp.dao.data.*;
import com.mofp.model.data.*;
import com.mofp.model.data.google.geocoding.GoogleAddressComponent;
import com.mofp.model.data.google.geocoding.GoogleGeocodingResult;
import com.mofp.model.data.google.geocoding.GoogleGeocodingResponse;
import com.mofp.model.data.openmap.weather.*;
import com.mofp.service.data.DistrictService;
import com.mofp.service.data.GoogleGeocodingService;
import com.mofp.service.data.OpenWeatherMapService;
import com.mofp.service.support.impl.DefaultBaseServiceImpl;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rivasyafri
 */
@Service
public class DefaultDistrictServiceImpl extends DefaultBaseServiceImpl<DistrictRepository> implements DistrictService {

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private GoogleGeocodingService googleGeocodingService;

    @Autowired
    private OpenWeatherMapService openWeatherMapService;

    @Override
    public District findOneOrCreateNewDistrict(double latitude, double longitude) {
        GoogleGeocodingResponse response = googleGeocodingService.getGoogleGeocodingResponse(latitude, longitude);
        return findOneOrCreateNewDistrict(response);
    }

    @Override
    public District findOneAndAddNewMovingObjectDataDistrict(@NonNull District district)
            throws NullPointerException {
        OpenMapWeatherFiveDayResponse openMapWeatherFiveDayResponse =
                openWeatherMapService.getOpenWeatherMapResponse(district);
        return findOneAndAddNewMovingObjectDataDistrict(openMapWeatherFiveDayResponse, district);
    }

    @Override
    public District pullDataOfDistrict(double latitude, double longitude) {
        GoogleGeocodingResponse geocodingResponse = googleGeocodingService.getGoogleGeocodingResponse(latitude, longitude);
        District district = this.findOneOrCreateNewDistrict(geocodingResponse);
        if (district != null) {
            OpenMapWeatherFiveDayResponse openMapResponse = openWeatherMapService.getOpenWeatherMapResponse(district);
            district = this.findOneAndAddNewMovingObjectDataDistrict(openMapResponse, district);
            return district;
        }
        return null;
    }

    @Override
    public List<Weather> getDataOfWeatherByDistrictAndTime(District district, Long startTime, Long endTime) {
        if (district == null) {
            return null;
        } else {
            Specification<Weather> specification =
                    (root, query, cb) ->
                            cb.and(
                                    cb.equal(root.get("district"), district),
                                    cb.and(
                                            cb.lessThanOrEqualTo(root.get("endTime"), endTime),
                                            cb.greaterThanOrEqualTo(root.get("startTime"), startTime)
                                    )
                            );
            return weatherRepository.findAll(specification);
        }
    }

    private District findOneOrCreateNewDistrict(@NonNull GoogleGeocodingResponse response)
            throws NullPointerException {
        ArrayList<GoogleGeocodingResult> results = new ArrayList<>(response.getResults());
        if (results != null) {
            GoogleGeocodingResult geocodingResult = results.get(0);
            List<GoogleAddressComponent> addressComponents = geocodingResult.getAddressComponents();
            String name = addressComponents.get(0).getLongName();
            if (addressComponents.size() > 1) {
                String country = addressComponents.get(addressComponents.size() - 1).getShortName();
                String state = null;
                if (addressComponents.size() > 2) {
                    state = addressComponents.get(addressComponents.size() - 2).getLongName();
                }
                District district = repository.findOneByNameLikeAndCountryLike(name, country);
                if (district == null) {
                    district = new District();
                    district.setName(name);
                    district.setState(state);
                    district.setCountry(country);
                    district = repository.saveAndFlush(district);
                }
                return district;
            }
        }
        return null;
    }

    private District findOneAndAddNewMovingObjectDataDistrict(@NonNull OpenMapWeatherFiveDayResponse response,
                                                              @NonNull District district)
            throws NullPointerException {
        ArrayList<OpenMapResultData> resultData = new ArrayList<>(response.getData());
        for (int i = 0; i < resultData.size(); i++) {
            OpenMapResultData result = resultData.get(i);
            Long startTime = result.getDt();
            Long endTime = startTime + (3 * 60 * 60);
            processingWeatherData(district, startTime, endTime, result);
        }
        district = repository.findOne(district.getId());
        return district;
    }

    private void processingWeatherData(District district, Long startTime, Long endTime, OpenMapResultData result) {
        Weather weather = weatherRepository.findOneByDistrictIdAndStartTime(district.getId(), startTime);
        if (weather == null) {
            OpenMapWeatherData weatherData = result.getMain();
            weather = new Weather();
            weather.setDistrict(district);
            weather.setStartTime(startTime);
            weather.setEndTime(endTime);
            weather.setRain(result.getRain().getRainOn3Hr());
            weather.setWindSpeed(result.getWind().getSpeed());
            weather.setWindDegree(result.getWind().getDegree());
            weather.setMaxTemperature(weatherData.getMaxTemperature());
            weather.setMinTemperature(weatherData.getMinTemperature());
            weather.setRelativeHumidity(weatherData.getHumidity());
            weatherRepository.saveAndFlush(weather);
        }
    }
}
