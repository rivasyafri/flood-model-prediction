package com.mofp.util;

import lombok.NonNull;
import org.apache.log4j.Logger;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.codec.Wkt;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rivasyafri
 */
public class WktGenerator {

    public static Polygon<G2D> createSquareBasedBorder(double northLat, double westLong, double southLat, double eastLong) {
        List<Pair<Double, Double>> points = new ArrayList<>();
        points.add(Pair.of(northLat, eastLong));
        points.add(Pair.of(southLat, eastLong));
        points.add(Pair.of(southLat, westLong));
        points.add(Pair.of(northLat, westLong));
        try {
            Polygon<G2D> polygon = createPolygonWkt(points);
            return polygon;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Polygon<G2D> createPolygonWkt(@NonNull List<Pair<Double, Double>> points)
            throws IllegalArgumentException, NullPointerException {
        if (points.size() < 3) {
            throw new IllegalArgumentException("List of points is consist of less than 3.");
        } else {
            StringBuilder wkt = new StringBuilder("SRID=4326;POLYGON((");
            for (int i = 0; i < points.size(); i++) {
                Pair<Double, Double> point = points.get(i);
                wkt.append(point.getSecond() + " " + point.getFirst() + ", ");
            }
            // Close the polygon
            Pair<Double, Double> point = points.get(0);
            wkt.append(point.getSecond() + " " + point.getFirst());
            wkt.append("))");
            return (Polygon<G2D>) Wkt.fromWkt(wkt.toString());
        }
    }
}
