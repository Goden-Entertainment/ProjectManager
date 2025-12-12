package org.example.projectmanager.controller;

import jakarta.servlet.http.HttpSession;
import org.example.projectmanager.model.Project;
import org.example.projectmanager.model.Team;
import org.example.projectmanager.model.User;
import org.example.projectmanager.model.userType;
import org.example.projectmanager.service.ProjectService;
import org.example.projectmanager.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("project")
public class ProjectController {

    private final TeamService teamService;
    private ProjectService projectService;

    public ProjectController(ProjectService projectService, TeamService teamService){
        this.projectService = projectService;
        this.teamService = teamService;
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

        List<Team> availableTeams = teamService.allAvailableTeamsFor_Project();

        model.addAttribute("availableTeams", availableTeams);
        model.addAttribute("newProject", new Project());
        return "addProjectForm";
    }

    @PostMapping("/add")
    public String createProject(@ModelAttribute Project project, @RequestParam List<Integer> selectedTeamIds, HttpSession session){
        User user = (User) session.getAttribute("user");

        if(user == null || user.getUserType() != userType.PROJECTMANAGER){
            return "redirect:/user/login";
        }

        int projectId = projectService.createProject(project);
        projectService.assignProjectToUser(projectId, user.getUserId());

        //ERROR HANDLING FOR WHEN NO TEAMS WERE SELECTED
        if(selectedTeamIds != null && !selectedTeamIds.isEmpty()){
            //CONNECTS THE TEAMS TO THE PROJECT WITH PROJECT_ID AS FOREIGN KEY
            for(Integer teamId: selectedTeamIds){
                Team selectedTeam = teamService.findTeam(teamId);
                selectedTeam.setProjectId(projectId);
                teamService.editTeam(selectedTeam);
            }
        }

        return "redirect:/project/myprojects";
    }

    @GetMapping("/edit/{projectId}")
    public String editProject(@PathVariable int projectId, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");

        if(user == null || user.getUserType() != userType.PROJECTMANAGER){
            return "redirect:/user/login";
        }

        Project project = projectService.findProject(projectId);
        List<Team> availableTeams = teamService.allAvailableTeamsFor_Project(projectId);

        model.addAttribute("availableTeams", availableTeams);
        model.addAttribute("project", project);
        return "editProjectForm";
    }

    @PostMapping("/edit")
    public String updateProject(@ModelAttribute Project project, @RequestParam(required = false) List<Integer> selectedTeamIds, HttpSession session){
        User user = (User) session.getAttribute("user");

        if(user == null || user.getUserType() != userType.PROJECTMANAGER){
            return "redirect:/user/login";
        }

        projectService.editProject(project);

        List<Team> projectTeams = teamService.getTeams(project.getProjectId(), null, null);
        List<Team> selectedTeams = new ArrayList<>();
        for(Integer teamId: selectedTeamIds) {
            Team selectedTeam = teamService.findTeam(teamId);
            selectedTeams.add(selectedTeam)
        }

        //REMOVE THE OLD TEAM TO PROJECT CONNECTIONS
        teamService.removeProjectTeams(project.getProjectId());

        //ERROR HANDLING FOR WHEN NO TEAMS WERE SELECTED
        if(selectedTeamIds != null && !selectedTeamIds.isEmpty()){
            //CONNECTS THE TEAMS TO THE PROJECT WITH PROJECT_ID AS FOREIGN KEY
            for(Integer teamId: selectedTeamIds) {
                Team selectedTeam = teamService.findTeam(teamId);
                selectedTeam.setProjectId(project.getProjectId());
                teamService.editTeam(selectedTeam);
            }
        }

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
