package com.mofp.service.impl;

import com.mofp.dao.ProjectRepository;
import com.mofp.dao.StateRepository;
import com.mofp.model.Project;
import com.mofp.service.GlobalService;
import com.mofp.service.ProjectService;
import org.apache.log4j.Logger;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.codec.Wkt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rivasyafri
 */
@Service
public class DefaultProjectServiceImpl implements ProjectService {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private GlobalService GLOBAL;

    @Override
    public Project run(long id, String selectedModel) {
        try {
            Project project = projectRepository.findOne(id);
            if (project.isDone()) {
                return project;
            } else if (project.getArea() != null) {
                logger.debug(project.getName() + " is running");
                project = GLOBAL.run(selectedModel, project);
                logger.debug(project.getName() + " is finished");
//                project.setDone(true);
                projectRepository.saveAndFlush(project);
                return project;
            } else {
                throw new Exception("Area is not specified");
            }
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Polygon createRectangleFromBounds(double northLat, double westLong,
                                             double southLat, double eastLong) {
        logger.debug("Create polygon using WKT initiated on "
                + northLat + ", "
                + westLong + ", "
                + southLat + ", "
                + eastLong);
        StringBuilder wkt = new StringBuilder("SRID=4326;POLYGON((");
        wkt.append(northLat + " " + eastLong + ", ");
        wkt.append(southLat + " " + eastLong + ", ");
        wkt.append(southLat + " " + westLong + ", ");
        wkt.append(northLat + " " + westLong + ", ");
        wkt.append(northLat + " " + eastLong);
        wkt.append("))");
        return (Polygon<G2D>) Wkt.fromWkt(wkt.toString());
    }
}
