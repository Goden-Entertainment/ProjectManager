package org.example.projectmanager.controller;

import jakarta.servlet.http.HttpSession;
import org.example.projectmanager.model.*;
import org.example.projectmanager.service.ProjectService;
import org.example.projectmanager.service.SubProjectService;
import org.example.projectmanager.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("subproject")
public class SubProjectController {

    private final TeamService teamService;
    private SubProjectService subProjectService;
    private ProjectService projectService;

    public SubProjectController(SubProjectService subProjectService, ProjectService projectService, TeamService teamService) {
        this.subProjectService = subProjectService;
        this.projectService = projectService;
        this.teamService = teamService;
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

        int totalActualTime = subProjectService.getTotalActualTime(projectId);

        model.addAttribute("totalActualTime", totalActualTime);
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

        List<Team> availableTeams = teamService.allAvailableTeamsFor_SubProject(projectId);
        SubProject newSubProject = new SubProject();
        newSubProject.setProjectId(projectId);

        model.addAttribute("availableTeams", availableTeams);
        model.addAttribute("newSubProject", newSubProject);
        return "addSubProjectForm";
    }

    @PostMapping("/add")
    public String createSubProject(@ModelAttribute SubProject subProject,
                                    @RequestParam(required = false) List<Integer> selectedTeamIds,
                                    HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        int subProjectId = subProjectService.createSubProject(subProject);

        //ERROR HANDLING FOR WHEN NO TEAMS WERE SELECTED
        if(selectedTeamIds != null && !selectedTeamIds.isEmpty()){
            //CONNECTS THE TEAMS TO THE SUBPROJECT WITH SUBPROJECT_ID AS FOREIGN KEY
            for(Integer teamId: selectedTeamIds) {
                Team selectedTeam = teamService.findTeam(teamId);
                selectedTeam.setSubProjectId(subProjectId);
                teamService.editTeam(selectedTeam);
            }
        }

        return "redirect:/subproject/list/" + subProject.getProjectId();
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

        List<Team> availableTeams = teamService.allAvailableTeamsFor_SubProject(subProject.getProjectId(),subProjectId);

        model.addAttribute("availableTeams", availableTeams);
        model.addAttribute("subProject", subProject);
        return "editSubProjectForm";
    }

    @PostMapping("/edit")
    public String updateSubProject(@ModelAttribute SubProject subProject,
                                    @RequestParam(required = false) List<Integer> selectedTeamIds,
                                    HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        subProjectService.editSubProject(subProject);

        //REMOVE THE OLD TEAM TO PROJECT CONNECTIONS
        List<Team> oldSelectedTeams = teamService.getTeams(subProject.getProjectId(), subProject.getProjectId(), null);
        for(Team team : oldSelectedTeams) {
            if(selectedTeamIds == null || !selectedTeamIds.contains(team.getTeamId())){
                team.setSubProjectId(null);
                team.setTaskId(null);

                teamService.editTeam(team);
            }
        }

        //ERROR HANDLING FOR WHEN NO TEAMS WERE SELECTED
        if(selectedTeamIds != null && !selectedTeamIds.isEmpty()){
            //CONNECTS THE TEAMS TO THE PROJECT WITH PROJECT_ID AS FOREIGN KEY
            for(Integer teamId: selectedTeamIds) {
                Team selectedTeam = teamService.findTeam(teamId);
                selectedTeam.setSubProjectId(subProject.getSubProjectId());
                teamService.editTeam(selectedTeam);
            }
        }
        return "redirect:/subproject/list/" + subProject.getProjectId();
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
