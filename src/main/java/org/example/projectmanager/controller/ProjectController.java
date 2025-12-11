package org.example.projectmanager.controller;

import jakarta.servlet.http.HttpSession;
import org.example.projectmanager.model.Project;
import org.example.projectmanager.model.User;
import org.example.projectmanager.model.userType;
import org.example.projectmanager.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("project")
public class ProjectController {

    private ProjectService projectService;

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @GetMapping("/myprojects")
    public String myProjects(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");

        if(user == null){
            return "redirect:/user/login";
        }

        if(user.getUserType() != userType.PROJECTMANAGER){
            return "redirect:/user/profile";
        }

        List<Project> projects = projectService.getProjectsByUserId(user.getUserId());
        model.addAttribute("projects", projects);
        return "projects";
    }

    @GetMapping("/add")
    public String addProject(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");

        if(user == null || user.getUserType() != userType.PROJECTMANAGER){
            return "redirect:/user/login";
        }


        model.addAttribute("newProject", new Project());
        return "addProjectForm";
    }

    @PostMapping("/add")
    public String createProject(@ModelAttribute Project project, HttpSession session){
        User user = (User) session.getAttribute("user");

        if(user == null || user.getUserType() != userType.PROJECTMANAGER){
            return "redirect:/user/login";
        }

        int projectId = projectService.createProject(project);
        projectService.assignProjectToUser(projectId, user.getUserId());

        return "redirect:/project/myprojects";
    }

    @GetMapping("/edit/{projectId}")
    public String editProject(@PathVariable int projectId, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");

        if(user == null || user.getUserType() != userType.PROJECTMANAGER){
            return "redirect:/user/login";
        }

        Project project = projectService.findProject(projectId);
        model.addAttribute("project", project);
        return "editProjectForm";
    }

    @PostMapping("/edit")
    public String updateProject(@ModelAttribute Project project, HttpSession session){
        User user = (User) session.getAttribute("user");

        if(user == null || user.getUserType() != userType.PROJECTMANAGER){
            return "redirect:/user/login";
        }

        projectService.editProject(project);
        return "redirect:/project/myprojects";
    }

    @GetMapping("/delete/{projectId}")
    public String deleteProject(@PathVariable int projectId, HttpSession session){
        User user = (User) session.getAttribute("user");

        if(user == null || user.getUserType() != userType.PROJECTMANAGER){
            return "redirect:/user/login";
        }

        projectService.deleteProject(projectId);
        return "redirect:/project/myprojects";
    }

    @GetMapping("/{projectId}")
    public String viewProject(@PathVariable int projectId, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");

        if(user == null || user.getUserType() != userType.PROJECTMANAGER){
            return "redirect:/user/login";
        }

        Project project = projectService.findProject(projectId);
        model.addAttribute("project", project);
        return "subprojects";
    }
}
