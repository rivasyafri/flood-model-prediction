package com.mofp.service;

import com.mofp.dao.ProjectRepository;
import com.mofp.model.Project;
import com.mofp.model.data.District;
import com.mofp.service.support.AbstractBaseService;
import lombok.NonNull;
import org.geolatte.geom.Polygon;
import org.springframework.data.util.Pair;

import java.util.List;

/**
 * @author rivasyafri
 */
public interface ProjectService extends AbstractBaseService<ProjectRepository> {

    /**
     * Run prediction project, if project already or still running than return the current results
     * @param id project id
     * @return finished project
     * @throws IllegalAccessException
     * @throws NullPointerException
     */
    Project checkOrRunPredictionProcess(long id) throws IllegalAccessException, NullPointerException;

    /**
     * Create rectangle bound
     * @param north north lat bound
     * @param west west lng bound
     * @param south south lat bound
     * @param east east lng bound
     * @return Polygon created from wkt
     */
    Polygon createRectangleBound(double north, double west, double south, double east)
            throws NullPointerException;

    /**
     * Create polygon using WKT
     * @param points pair of latitude and longitude minimum of 3 not null
     * @return Polygon created from wkt
     * @throws IllegalArgumentException
     * @throws NullPointerException
     */
    Polygon createPolygonWkt(@NonNull List<Pair<Double, Double>> points) throws IllegalArgumentException, NullPointerException;

    /**
     * Reset by removing all result related to project
     * @param id id of project
     * @return
     */
    Project resetProject(long id);
}
