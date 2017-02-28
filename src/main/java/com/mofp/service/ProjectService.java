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
     * Reset by removing all result related to project
     * @param id id of project
     * @return
     */
    Project resetProject(long id);
}
