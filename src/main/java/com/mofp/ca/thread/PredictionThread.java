package com.mofp.ca.thread;

import com.mofp.ca.dao.ProjectRepository;
import com.mofp.ca.model.GlobalFloodPrediction;
import com.mofp.ca.model.Project;
import com.mofp.ca.service.ProjectService;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author rivasyafri
 */
@Component
@Scope("prototype")
public class PredictionThread implements Runnable {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Setter
    private String name;
    @Setter
    private Project project;
    @Getter
    private boolean finished = false;

    public GlobalFloodPrediction GLOBAL = new GlobalFloodPrediction();

    public PredictionThread(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        // Add process prediction here
        logger.debug(name + " is running");
        // For all Cell in Grid, calculate runoff
        GLOBAL.initialize();
        GLOBAL.run();
        logger.debug(name + " is finished");
        // When done
        project.setDone(true);
        projectRepository.save(project);
        this.finished = true;
    }
}
