package com.mofp.ca.thread;

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

    @Setter
    private String name;
    @Setter
    private Project project;
    @Getter
    private boolean finished = false;

    public GlobalFloodPrediction GLOBAL;

    public PredictionThread() {
        this.name = "prediction";
    }

    public PredictionThread(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        logger.debug(name + " is running");
        if (project != null) {
            GLOBAL = new GlobalFloodPrediction(project);
        } else {
            GLOBAL = new GlobalFloodPrediction();
        }
//        GLOBAL.run();
        logger.debug(name + " is finished");
        project.setDone(true);
//        projectService.saveProject(PROJECT);
        this.finished = true;
    }
}
