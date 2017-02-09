package com.mofp.model.support.json;

import com.fasterxml.jackson.core.JsonParser;
import org.geolatte.common.dataformats.json.jackson.AbstractJsonDeserializer;
import org.geolatte.common.dataformats.json.jackson.JsonMapper;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.codec.Wkt;
import org.geolatte.geom.crs.CrsId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author rivasyafri
 */
public class PolygonDeserializer extends AbstractJsonDeserializer<Polygon>{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public PolygonDeserializer() {
        super(new JsonMapper());
    }

    @Override
    @SuppressWarnings({"unchecked"})
    protected Polygon deserialize(JsonParser jsonParser) throws IOException {
        String type = getStringParam("type", "Invalid GeoJSON, type property required.");
        //TODO -- spec also states that if CRS element is null, no CRS should be assumed.
        // Default srd = WGS84 according to the GeoJSON specification
        Integer srid = getSrid();
        CrsId crsId = srid == null ? parent.getDefaultCrsId() : CrsId.valueOf(srid);
        List coordinates = getTypedParam("coordinates", "Invalid or missing coordinates property", ArrayList.class);
        // We risk a classcast exception for each of these calls, since every call specifically states which list
        // he needs.
        try {
            if ("Polygon".equals(type)) {
                Polygon<G2D> result = asPolygon(coordinates, crsId);
                if (getDeserializerClass().isAssignableFrom(Polygon.class)) {
                    return result;
                } else {
                    throw new IOException("Json is a valid Polygon serialization, but this does not correspond with " +
                            "the expected outputtype of the deserializer (" + getDeserializerClass().getSimpleName() + ")");

                }
            } else {
                throw new IOException("Unknown type for a geometry deserialization");
            }
        } catch (ClassCastException e) {
            // this classcast can be thrown since coordinates is passed on as a List but the different methods
            // expect a specific list, so an implicit cast is performed there.
            throw new IOException("Coordinate array is not of expected type with respect to given type parameter.");
        }
    }

    /**
     * Parses the JSON as a polygon geometry
     *
     * @param coords the coordinate array corresponding with the polygon (a list containing rings, each of which
     *               contains a list of coordinates (which in turn are lists of numbers)).
     * @param crsId
     * @return An instance of polygon
     * @throws IOException if the given json does not correspond to a polygon or can be parsed as such.
     */
    private Polygon asPolygon(List<List<List>> coords, CrsId crsId) throws IOException {
        if (coords == null || coords.isEmpty()) {
            throw new IOException("A polygon requires the specification of its outer ring");
        }
        try {
            StringBuilder wkt = new StringBuilder("POLYGON(");
            int i = 0;
            for (List<List> ring : coords) {
                if (i != 0) {
                    wkt.append(",");
                }
                String ringCoords = createWktPolygon(ring, crsId);
                wkt.append(ringCoords);
                i++;
            }
            wkt.append(")");
            logger.debug(wkt.toString());
            return (Polygon) Wkt.fromWkt(wkt.toString());
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid Polygon: " + e.getMessage(), e);
        }

    }

    /**
     * This method takes in a list of lists and returns a <code>PointSequence</code> that correspond with that list.  The elements
     * in the outer list are lists that contain numbers (either integers or doubles). Each of those lists must have
     * at least two values, which are interpreted as x and y. If a third value is present, it is interpreted as the z value.
     * If more than three values are present, they are ignored. This is consistent with the geojson specification that states:
     * <i>
     * A position is represented by an array of numbers. There must be at least two elements, and may be more.
     * The order of elements must follow x, y, z order (easting, northing, altitude for coordinates in a projected
     * coordinate reference system, or longitude, latitude, altitude for coordinates in a geographic coordinate
     * reference system). Any number of additional elements are allowed -- interpretation and meaning of additional
     * elements is beyond the scope of this specification
     * </i>
     *
     * @param entry a list of lists of numbers representing a list of points.
     * @return an <code>PointSequence</code> containing the list of points.
     * @throws IOException if the conversion can not be executed (eg because one of the innerlists contains more or
     *                     less than two doubles.
     */
    private String createWktPolygon(List<List> entry, CrsId crsId) throws IOException {
        StringBuilder wkt = new StringBuilder("(");
        for (int i = 0; i < entry.size(); i++) {
            if (i != 0) {
                wkt.append(", ");
            }
            List current = entry.get(i);
            if (current.size() < 2) {
                throw new IOException("A coordinate must always contain at least two numbers");
            }
            for (Object value : current) {
                if (!(value instanceof Integer || value instanceof Double)) {
                    throw new IOException("A coordiante only permits numbers.");
                }
            }
            Double type = null;
            Double x = parseDefault(String.valueOf(current.get(0)), type);
            Double y = parseDefault(String.valueOf(current.get(1)), type);
            if (x == null || y == null) {
                // I don't see how this is possible....So you won't be able to unittest this case I think.
                throw new IOException("Unexpected number format for coordinate?");
            }
            wkt.append(x + " " + y);
        }
        wkt.append(")");
        logger.debug(wkt.toString());
        return wkt.toString();
    }

    /**
     * @return the class of the object that this deserializer can deserialize
     */
    protected Class getDeserializerClass()
    {
        return Polygon.class;
    }

    /**
     * If an srid (crs) is specified in the json object, it is returned. If no srid is found in the current parameter-map
     * null is returned. This is a simplified version from the actual GeoJSON specification, since the GeoJSON specification
     * allows for relative links to either URLS or local files in which the crs should be defined. This implementation
     * only supports named crs's: namely:
     * <pre>
     *  "crs": {
     *       "type": "name",
     *       "properties": {
     *             "name": "...yourcrsname..."
     *       }
     * }
     * </pre>
     * Besides the fact that only named crs is permitted for deserialization, the given name must either be of the form:
     * <pre>
     *  urn:ogc:def:EPSG:x.y:4326
     * </pre>
     * (with x.y the version of the EPSG) or of the form:
     * <pre>
     * EPSG:4326
     * </pre>
     * @return the SRID value of the crs system in the json if it is present, null otherwise.
     * @throws java.io.IOException If a crs object is present, but deserialization is not possible
     */
    protected Integer getSrid() throws IOException {
        Map<String, Object> crsContent = getTypedParam("crs", null, Map.class);
        if (crsContent != null) {
            if (crsContent.get("type") == null || !"name".equals(crsContent.get("type"))) {
                throw new IOException("If the crs is specified the type must be specified. Currently, only named crses are supported.");
            }
            Object properties = crsContent.get("properties");
            if (properties == null || !(properties instanceof Map) || (!((Map) properties).containsKey("name"))) {
                throw new IOException("A crs specification requires a properties value containing a name value.");
            }
            String sridString = ((Map) properties).get("name").toString();
            if (sridString.startsWith("EPSG:")) {
                Integer srid = parseDefault(sridString.substring(5), (Integer) null);
                if (srid == null) {
                    throw new IOException("Unable to derive SRID from crs name");
                } else {
                    return srid;
                }
            } else if (sridString.startsWith("urn:ogc:def:crs:EPSG:")) {
                String[] splits = sridString.split(":");
                if (splits.length != 7) {
                    throw new IOException("Unable to derive SRID from crs name");
                } else {
                    Integer srid = parseDefault(splits[6], (Integer) null);
                    if (srid == null) {
                        throw new IOException("Unable to derive SRID from crs name");
                    }
                    return srid;
                }
            } else {
                throw new IOException("Unable to derive SRID from crs name");
            }
        }
        return null;
    }
}
