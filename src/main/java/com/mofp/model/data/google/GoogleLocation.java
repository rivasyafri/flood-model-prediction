package com.mofp.model.data.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * @author rivasyafri
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleLocation {

    @Getter
    @Setter
    private double lat;

    @Getter
    @Setter
    private double lng;
}
