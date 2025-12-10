package org.example.projectmanager.controller;

import jakarta.servlet.http.HttpSession;
import org.example.projectmanager.model.Team;
import org.example.projectmanager.model.User;
import org.example.projectmanager.model.userType;
import org.example.projectmanager.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("team")
public class TeamController {

    private TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    // LIST - Show all teams with their members
    @GetMapping("/list")
    public String listTeams(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        // Get all teams
        List<Team> teams= teamService.getTeams();

        // Lazy load team members - create map of teamId -> List<User>
        Map<Integer, List<User>> teamDevMapping = new HashMap<>();
        for (Team team : teams) {
            List<User> teamDevs = teamService.getTeamDevs(team.getTeamId());
            teamDevMapping.put(team.getTeamId(), teamDevs);
        }

        model.addAttribute("teams", teams);
        model.addAttribute("teamDevMapping", teamDevMapping);
        return "teams";
    }

    // ADD - Show form
    @GetMapping("/add")
    public String addTeam(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        model.addAttribute("newTeam", new Team());
        model.addAttribute("allDevs", teamService.getAllDevs());
        return "addTeamForm";
    }

    // ADD - Process form
    @PostMapping("/add")
    public String createTeam(@ModelAttribute Team team,
                             @RequestParam(value = "selectedDevs", required = false) List<Integer> selectedDevs,
                             HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        // Create team first
        int teamId = teamService.createTeam(team);

        // Assign selected devs to team (if any)
        if (selectedDevs != null && !selectedDevs.isEmpty()) {
            for (Integer devId : selectedDevs) {
            }
        }

        return "redirect:/team/list";
    }

    // EDIT - Show form
    @GetMapping("/edit/{teamId}")
    public String editTeam(@PathVariable int teamId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        Team team = teamService.findTeam(teamId);

        model.addAttribute("team", team);
        return "editTeamForm";
    }

    // EDIT - Process form
    @PostMapping("/edit")
    public String updateTeam(@ModelAttribute Team team,
                             @RequestParam(value = "selectedDevs", required = false) List<Integer> selectedDevs,
                             HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        // Update team info
        teamService.editTeam(team);

        // Get current members
        List<User> currentMembers = teamService.getTeamDevs(team.getTeamId());


        return "redirect:/team/list";
    }

    // DELETE
    @GetMapping("/delete/{teamId}")
    public String deleteTeam(@PathVariable int teamId, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        teamService.deleteTeam(teamId);
        return "redirect:/team/list";
    }
}
