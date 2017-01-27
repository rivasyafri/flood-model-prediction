package com.mofp.ca.service;

import com.mofp.ca.model.Project;
import org.geolatte.geom.Polygon;

/**
 * @author rivasyafri
 */
public interface ProjectService {

    Project run(long id);
    Polygon createRectangleFromBounds(double north, double west, double south, double east);
}
