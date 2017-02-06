package com.mofp.controller;

import com.mofp.dao.ProjectRepository;
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
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @RequestMapping(value = "/project/run", method = RequestMethod.GET)
    public @ResponseBody Project run(@RequestParam(value = "id") long id,
                                     @RequestParam(value = "model") String selectedModel) {
        return projectService.run(id, selectedModel);
    }

    @RequestMapping(value = "/project/setBorder", method = RequestMethod.POST)
    public @ResponseBody Project setBorder(@RequestParam(value = "id") long id,
                                           @RequestParam(value = "north") double north,
                                           @RequestParam(value = "west") double west,
                                           @RequestParam(value = "south") double south,
                                           @RequestParam(value = "east") double east) {
        Project project = projectRepository.findOne(id);
        if (project != null) {
            project.setArea(projectService.createRectangleFromBounds(north, west, south, east));
            projectRepository.saveAndFlush(project);
            return project;
        }
        return null;
    }
}
