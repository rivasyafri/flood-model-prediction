package com.mofp.model.support.json;

import java.io.IOException;

import org.geolatte.common.dataformats.json.jackson.JsonMapper;
import org.geolatte.common.dataformats.json.jackson.PolygonSerializer;
import org.geolatte.geom.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Get from https://github.com/GeoLatte/geolatte-common/issues/30
 * Change from C2D to G2D
 */
public class PolygonToGeoJSON extends PolygonSerializer {

    public PolygonToGeoJSON() {
        super(new JsonMapper());
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected void writeShapeSpecificSerialization(Polygon value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        jgen.writeFieldName("type");
        jgen.writeString("Polygon");
        jgen.writeArrayFieldStart("coordinates");
        JsonSerializer<Object> ser = provider.findValueSerializer(Double.class, null);
        writeLineString(value.getExteriorRing(), jgen, provider, ser);
        for (int i = 0; i < value.getNumInteriorRing(); i++) {
            writeLineString(value.getInteriorRingN(i), jgen, provider, ser);
        }
        jgen.writeEndArray();
    }

    /**
     * Writes a ring to the json
     */
    protected void writeLineString(LineString<G2D> line, JsonGenerator jgen, SerializerProvider provider,
                                   JsonSerializer<Object> ser) throws IOException {

        jgen.writeStartArray();
        for (int i = 0; i < line.getNumPositions(); i++) {

            jgen.writeStartArray();
            G2D position = line.getPositionN(i);
            ser.serialize(position.getLat(), jgen, provider);
            ser.serialize(position.getLon(), jgen, provider);
            jgen.writeEndArray();
        }

        jgen.writeEndArray();
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected double[] envelopeToCoordinates(Envelope e) {

        Position upperLeft = e.upperLeft();
        Position lowerRight = e.lowerRight();
        return new double[]{lowerRight.getCoordinate(0), lowerRight.getCoordinate(1), upperLeft.getCoordinate(0),
                upperLeft.getCoordinate(1)};
    }
}
