package com.mofp.model.data.google.elevation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author rivasyafri
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleElevationResponse {

    @Getter
    @Setter
    private List<Elevation> results;

    @Getter
    @Setter
    private String status;
}
