package com.mofp.model.data.google.elevation;

import com.mofp.model.data.google.GoogleLocation;
import lombok.Getter;
import lombok.Setter;

/**
 * @author rivasyafri
 */
public class GoogleElevation {

    @Getter
    @Setter
    private double elevation;

    @Getter
    @Setter
    private GoogleLocation location;

    @Getter
    @Setter
    private double resolution;
}
