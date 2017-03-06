package com.mofp.model.data.openmap.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author rivasyafri
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMapResultData {

    @Getter
    @Setter
    private Long dt;

    @Getter
    @Setter
    @JsonProperty("dt_txt")
    private String textDateTime;

    @Getter
    @Setter
    private OpenMapWeatherData main;

    @Getter
    @Setter
    private OpenMapCloudsData clouds;

    @Getter
    @Setter
    private OpenMapWindData wind;

    @Getter
    @Setter
    private OpenMapRainData rain;

    @Getter
    @Setter
    private OpenMapSnowData snow;
}
