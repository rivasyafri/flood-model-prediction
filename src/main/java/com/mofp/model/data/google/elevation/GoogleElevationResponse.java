package com.mofp.model.data.google.elevation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mofp.model.data.google.GoogleResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author rivasyafri
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleElevationResponse extends GoogleResponse {

    @Getter
    @Setter
    private List<GoogleElevation> results;
}
