package com.mofp.thread;

import com.mofp.service.GlobalService;
import com.mofp.service.impl.DefaultGlobalServiceImpl;
import com.mofp.model.Project;
import com.mofp.service.ProjectService;
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

    @Setter
    private String name;
    @Setter
    private Project project;
    @Getter
    private boolean finished = false;

    public GlobalService GLOBAL;

    public PredictionThread() {
        this.name = "service";
    }

    public PredictionThread(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        logger.debug(name + " is running");
//        GLOBAL.run(project);
        logger.debug(name + " is finished");
//        project.setDone(true);
        this.finished = true;
    }
}
