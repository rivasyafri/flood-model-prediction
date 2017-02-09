package com.mofp.service;

import com.mofp.model.Project;
import org.geolatte.geom.Polygon;

/**
 * @author rivasyafri
 */
public interface ProjectService {

    Project run(long id, String selectedModel);
    Polygon createRectangleFromBounds(double north, double west, double south, double east);
}
