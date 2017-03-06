package com.mofp.model.data.openmap.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mofp.model.data.openmap.OpenMapCoordinate;
import lombok.Getter;
import lombok.Setter;

/**
 * @author rivasyafri
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMapCity {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    @JsonProperty("coord")
    private OpenMapCoordinate coordinate;

    @Getter
    @Setter
    private String country;
}
