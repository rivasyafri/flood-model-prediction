package com.mofp.model.data.openmap.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author rivasyafri
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMapSnowData {

    @Getter
    @Setter
    @JsonProperty("3hr")
    private Double snowOn3Hr;
}
