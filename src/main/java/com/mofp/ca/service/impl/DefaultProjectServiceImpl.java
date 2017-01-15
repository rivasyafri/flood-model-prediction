package com.mofp.ca.service.impl;

import com.mofp.AppConfig;
import com.mofp.ca.thread.PredictionThread;
import com.mofp.ca.dao.ProjectRepository;
import com.mofp.ca.model.Project;
import com.mofp.ca.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    private PredictionThread predictionThread;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Project run(long id) {
        try {
            Project project = projectRepository.findOne(id);
            if (project.isDone() || project.getCellList() != null) {
                return project;
            } else {
                ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
                ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context.getBean("taskExecutor");
                if (taskExecutor.getActiveCount() == 0) {
                    predictionThread = (PredictionThread) context.getBean("CellularAutomata");
                    predictionThread.setName("project id = " + id);
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
}
