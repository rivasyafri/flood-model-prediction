package com.mofp.model.data.google.geocoding;

import com.mofp.model.data.google.GoogleLocation;
import lombok.Getter;
import lombok.Setter;

/**
 * @author rivasyafri
 */
public class GoogleViewPort {

    @Getter
    @Setter
    private GoogleLocation northeast;

    @Getter
    @Setter
    private GoogleLocation southwest;
}
