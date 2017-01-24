package com.mofp.ca.model.json;

import org.geolatte.common.dataformats.json.jackson.GeometryDeserializer;
import org.geolatte.common.dataformats.json.jackson.JsonMapper;
import org.geolatte.geom.Polygon;

/**
 * @author rivasyafri
 */
public class PolygonDeserializer extends GeometryDeserializer<Polygon> {

    public PolygonDeserializer() {
        super(new JsonMapper(), Polygon.class);
    }
}
