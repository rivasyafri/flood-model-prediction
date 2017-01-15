package com.mofp.ca.thread;

import com.mofp.ca.dao.ProjectRepository;
import com.mofp.ca.model.Project;
import com.mofp.ca.service.ProjectService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author rivasyafri
 */
@Component
@Scope("prototype")
public class PredictionThread implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Setter
    String name;

    @Setter
    private Project project;

    public PredictionThread(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        // Add process prediction here
        logger.debug(name + " is running");
        // For all Cell in Grid, calculate runoff

        logger.debug(name + " is finished");
        // When done
        project.setDone(true);
        projectRepository.save(project);
    }
}
