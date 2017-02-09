package com.mofp.model.data.google.elevation;

import lombok.Getter;
import lombok.Setter;

/**
 * @author rivasyafri
 */
public class Elevation {

    @Getter
    @Setter
    private double elevation;

    @Getter
    @Setter
    private Location location;

    @Getter
    @Setter
    private double resolution;
}
