package com.mofp.ca.service.impl;

import com.mofp.AppConfig;
import com.mofp.ca.dao.ProjectRepository;
import com.mofp.ca.model.Project;
import com.mofp.ca.service.ProjectService;
import com.mofp.ca.thread.PredictionThread;
import org.apache.log4j.Logger;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.codec.Wkt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

/**
 * @author rivasyafri
 */
@Service
public class DefaultProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    private final Logger logger = Logger.getLogger(getClass());

    @Override
    public Project run(long id) {
        try {
            Project project = projectRepository.findOne(id);
//            if (project.isDone() || project.getCellList().size() != 0) {
            if (project.getCellList().size() != 0) {
                return project;
            } else {
                ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
                ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context.getBean("taskExecutor");
                if (taskExecutor.getActiveCount() == 0) {
                    PredictionThread predictionThread = (PredictionThread) context.getBean("CellularAutomata");
                    predictionThread.setName("project id -" + project.getId() +
                            "- with name '"+ project.getName() +"'");
                    predictionThread.setProject(project);
                    taskExecutor.execute(predictionThread);
                } else {
                    logger.debug("There is another instance thread still running.");
                }
                return project;
            }
        } catch (Exception e) {
            logger.error("Project have to be saved first/n" + e.toString());
            return null;
        }
    }

    @Override
    public Polygon createRectangleFromBounds(double northLat, double westLong,
                                             double southLat, double eastLong) {
        logger.debug("Create polygon using WKT initiated");
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
