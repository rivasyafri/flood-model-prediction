package com.mofp.model.data.openmap.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author rivasyafri
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMapWeatherData {

    @Getter
    @Setter
    @JsonProperty("temp")
    private Double temperature;

    @Getter
    @Setter
    @JsonProperty("temp_min")
    private Double minTemperature;

    @Getter
    @Setter
    @JsonProperty("temp_max")
    private Double maxTemperature;

    @Getter
    @Setter
    private Double pressure;

    @Getter
    @Setter
    @JsonProperty("sea_level")
    private Double seaLevel;

    @Getter
    @Setter
    @JsonProperty("grnd_level")
    private Double groundLevel;

    @Getter
    @Setter
    @JsonProperty("humidity")
    private Double humidity;

    @Getter
    @Setter
    @JsonProperty("temp_kf")
    private Double tempKf;
}