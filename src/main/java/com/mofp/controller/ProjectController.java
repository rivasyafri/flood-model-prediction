package com.mofp.controller;

import com.mofp.model.Project;
import com.mofp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author rivasyafri
 */
@Controller
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping(value = "/project/run", method = RequestMethod.POST)
    public @ResponseBody Project run(@RequestParam(value = "id") long id) {
        try {
            Project project = projectService.checkOrRunPredictionProcess(id);
            return project;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/project/setBorder", method = RequestMethod.POST)
    public @ResponseBody Project setBorder(@RequestParam(value = "id") long id,
                                           @RequestParam(value = "north") double north,
                                           @RequestParam(value = "west") double west,
                                           @RequestParam(value = "south") double south,
                                           @RequestParam(value = "east") double east) {
        Project project = projectService.getRepository().findOne(id);
        if (project != null) {
            project.setArea(projectService.createRectangleBound(north, west, south, east));
            projectService.getRepository().saveAndFlush(project);
            return project;
        }
        return null;
    }

    @RequestMapping(value = "/project/result", method = RequestMethod.GET)
    public @ResponseBody Project getResult(@RequestParam(value = "id") long id) {
        Project project = projectService.getRepository().findOne(id);
        if (project != null) {
            return project;
        }
        return null;
    }

    @RequestMapping(value = "/project/reset", method = RequestMethod.GET)
    public @ResponseBody Project reset(@RequestParam(value = "id") long id) {
        Project project = projectService.resetProject(id);
        if (project != null) {
            return project;
        }
        return null;
    }
}
