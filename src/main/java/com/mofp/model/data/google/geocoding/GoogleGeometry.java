package com.mofp.model.data.google.geocoding;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mofp.model.data.google.GoogleLocation;
import lombok.Getter;
import lombok.Setter;

/**
 * @author rivasyafri
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleGeometry {

    @Getter
    @Setter
    private GoogleLocation location;

    @Getter
    @Setter
    @JsonProperty("location_type")
    private String locationType;

    @Getter
    @Setter
    private GoogleViewPort viewPort;
}
