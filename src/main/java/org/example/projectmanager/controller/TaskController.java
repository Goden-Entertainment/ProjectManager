package org.example.projectmanager.controller;

import jakarta.servlet.http.HttpSession;
import org.example.projectmanager.model.SubProject;
import org.example.projectmanager.model.Task;
import org.example.projectmanager.model.Team;
import org.example.projectmanager.model.User;
import org.example.projectmanager.model.userType;
import org.example.projectmanager.service.SubProjectService;
import org.example.projectmanager.service.TaskService;
import org.example.projectmanager.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("task")
public class TaskController {

    private TaskService taskService;
    private SubProjectService subProjectService;
    private TeamService teamService;

    public TaskController(TaskService taskService, SubProjectService subProjectService, TeamService teamService) {
        this.taskService = taskService;
        this.subProjectService = subProjectService;
        this.teamService = teamService;
    }

    @GetMapping("/list/{subProjectId}")
    public String listTasks(@PathVariable int subProjectId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        SubProject subProject = subProjectService.findSubProject(subProjectId);
        List<Task> tasks = taskService.getTasksBySubProjectId(subProjectId);

        int totalActualTime = taskService.getTotalActualTime(subProjectId);

        model.addAttribute("totalActualTime", totalActualTime);
        model.addAttribute("tasks", tasks);
        model.addAttribute("subProject", subProject);
        return "tasks";
    }

    @GetMapping("/add/{subProjectId}")
    public String addTask(@PathVariable int subProjectId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        List<Team> availableTeams = teamService.allAvailableTeamsFor_Task(subProjectId);
        Task newTask = new Task();
        newTask.setSubProjectId(subProjectId);

        model.addAttribute("newTask", newTask);
        model.addAttribute("availableTeams", availableTeams);
        return "addTaskForm";
    }

    @PostMapping("/add")
    public String createTask(@ModelAttribute Task task,
                              @RequestParam(required = false) List<Integer> selectedTeamIds,
                              HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        int taskId = taskService.createTask(task);

        //ERROR HANDLING FOR WHEN NO TEAMS WERE SELECTED
        if(selectedTeamIds != null && !selectedTeamIds.isEmpty()){
            //CONNECTS THE TEAMS TO THE SUBPROJECT WITH SUBPROJECT_ID AS FOREIGN KEY
            for(Integer teamId: selectedTeamIds) {
                Team selectedTeam = teamService.findTeam(teamId);
                selectedTeam.setTaskId(taskId);
                teamService.editTeam(selectedTeam);
            }
        }

        return "redirect:/task/list/" + task.getSubProjectId();
    }

    @GetMapping("/edit/{taskId}")
    public String editTask(@PathVariable int taskId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        Task task = taskService.findTask(taskId);

        // Get all teams for dropdown
        List<Team> availableTeams = teamService.allAvailableTeamsFor_Task(task.getSubProjectId(),taskId);

        model.addAttribute("task", task);
        model.addAttribute("availableTeams", availableTeams);
        return "editTaskForm";
    }

    @PostMapping("/edit")
    public String updateTask(@ModelAttribute Task task,
                              @RequestParam(required = false) List<Integer> selectedTeamIds,
                              HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        taskService.editTask(task);

        //REMOVE THE OLD TEAM TO PROJECT CONNECTIONS
        List<Team> oldSelectedTeams = teamService.getTeams(null, null, task.getTaskId());
        for(Team team : oldSelectedTeams) {
            if(selectedTeamIds == null || !selectedTeamIds.contains(team.getTeamId())){
                team.setTaskId(null);

                teamService.editTeam(team);
            }
        }

        //ERROR HANDLING FOR WHEN NO TEAMS WERE SELECTED
        if(selectedTeamIds != null && !selectedTeamIds.isEmpty()){
            //CONNECTS THE TEAMS TO THE PROJECT WITH PROJECT_ID AS FOREIGN KEY
            for(Integer teamId: selectedTeamIds) {
                Team selectedTeam = teamService.findTeam(teamId);
                selectedTeam.setTaskId(task.getTaskId());
                teamService.editTeam(selectedTeam);
            }
        }
        return "redirect:/task/list/" + task.getSubProjectId();
    }

    @GetMapping("/delete/{taskId}")
    public String deleteTask(@PathVariable int taskId, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        // Get subproject ID before deleting (simplified)
        int subProjectId = taskService.getSubProjectIdByTaskId(taskId);

        taskService.deleteTask(taskId);
        return "redirect:/task/list/" + subProjectId;
    }
}
