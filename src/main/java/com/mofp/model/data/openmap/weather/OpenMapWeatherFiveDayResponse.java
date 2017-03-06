package com.mofp.model.data.openmap.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author rivasyafri
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMapWeatherFiveDayResponse {

    @Getter
    @Setter
    private String cod;

    @Getter
    @Setter
    @JsonProperty("cnt")
    private Integer numberOfData;

    @Getter
    @Setter
    @JsonProperty("list")
    private List<OpenMapResultData> data;

    @Getter
    @Setter
    private OpenMapCity city;
}
