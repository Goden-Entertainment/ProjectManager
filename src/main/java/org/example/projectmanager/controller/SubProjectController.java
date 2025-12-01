package org.example.projectmanager.controller;

import jakarta.servlet.http.HttpSession;
import org.example.projectmanager.model.Project;
import org.example.projectmanager.model.SubProject;
import org.example.projectmanager.model.User;
import org.example.projectmanager.model.userType;
import org.example.projectmanager.service.ProjectService;
import org.example.projectmanager.service.SubProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("subproject")
public class SubProjectController {

    private SubProjectService subProjectService;
    private ProjectService projectService;

    public SubProjectController(SubProjectService subProjectService, ProjectService projectService) {
        this.subProjectService = subProjectService;
        this.projectService = projectService;
    }

    @GetMapping("/list/{projectId}")
    public String listSubProjects(@PathVariable int projectId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        Project project = projectService.findProject(projectId);
        List<SubProject> subProjects = subProjectService.getSubProjectsByProjectId(projectId);

        model.addAttribute("project", project);
        model.addAttribute("subProjects", subProjects);
        return "subprojects";
    }

    @GetMapping("/add/{projectId}")
    public String addSubProject(@PathVariable int projectId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        model.addAttribute("newSubProject", new SubProject());
        model.addAttribute("projectId", projectId);
        return "addSubProjectForm";
    }

    @PostMapping("/add")
    public String createSubProject(@ModelAttribute SubProject subProject,
                                    @RequestParam("projectId") int projectId,
                                    HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        // Set FK directly and create subproject (simplified - no junction table)
        subProject.setProjectId(projectId);
        subProjectService.createSubProject(subProject);

        return "redirect:/subproject/list/" + projectId;
    }

    @GetMapping("/edit/{subProjectId}")
    public String editSubProject(@PathVariable int subProjectId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        SubProject subProject = subProjectService.findSubProject(subProjectId);

        // Get project ID directly from FK (simplified)
        int projectId = subProjectService.getProjectIdBySubProjectId(subProjectId);

        model.addAttribute("subProject", subProject);
        model.addAttribute("projectId", projectId);
        return "editSubProjectForm";
    }

    @PostMapping("/edit")
    public String updateSubProject(@ModelAttribute SubProject subProject,
                                    @RequestParam("projectId") int projectId,
                                    HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        subProjectService.editSubProject(subProject);
        return "redirect:/subproject/list/" + projectId;
    }

    @GetMapping("/delete/{subProjectId}")
    public String deleteSubProject(@PathVariable int subProjectId, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        // Get project ID before deleting (simplified)
        int projectId = subProjectService.getProjectIdBySubProjectId(subProjectId);

        subProjectService.deleteSubProject(subProjectId);
        return "redirect:/subproject/list/" + projectId;
    }
}
