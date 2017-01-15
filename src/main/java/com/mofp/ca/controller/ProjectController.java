package com.mofp.ca.controller;

import com.mofp.ca.dao.ProjectRepository;
import com.mofp.ca.model.Cell;
import com.mofp.ca.model.Project;
import com.mofp.ca.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
    public @ResponseBody Project run(@RequestParam(value = "id") long id) {
        return projectService.run(id);
    }

    @RequestMapping(value = "/project/isDone", method = RequestMethod.GET)
    public @ResponseBody boolean isDone(@RequestParam(value = "id") long id) {
        Project project = projectRepository.findOne(id);
        if (project != null) {
            if (project.isDone()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
