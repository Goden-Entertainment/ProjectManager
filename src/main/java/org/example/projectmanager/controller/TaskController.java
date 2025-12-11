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

        // Get project ID for back button (simplified - no junction table)
        int projectId = subProjectService.getProjectIdBySubProjectId(subProjectId);

        int totalActualTime = taskService.getTotalActualTime(subProjectId);

        model.addAttribute("totalActualTime", totalActualTime);
        model.addAttribute("subProject", subProject);
        model.addAttribute("tasks", tasks);
        model.addAttribute("projectId", projectId);
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

        // Get all teams for dropdown
        List<Team> allTeams = teamService.getTeams();

        model.addAttribute("newTask", new Task());
        model.addAttribute("teams", allTeams);
        model.addAttribute("subProjectId", subProjectId);
        return "addTaskForm";
    }

    @PostMapping("/add")
    public String createTask(@ModelAttribute Task task,
                              @RequestParam("subProjectId") int subProjectId,
                              HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        // Set FK directly and create task (simplified - no junction table)
        task.setSubProjectId(subProjectId);
        taskService.createTask(task);

        return "redirect:/task/list/" + subProjectId;
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

        // Get subproject ID directly from FK (simplified)
        int subProjectId = taskService.getSubProjectIdByTaskId(taskId);

        // Get all teams for dropdown
        List<Team> allTeams = teamService.getTeams();

        model.addAttribute("task", task);
        model.addAttribute("teams", allTeams);
        model.addAttribute("subProjectId", subProjectId);
        return "editTaskForm";
    }

    @PostMapping("/edit")
    public String updateTask(@ModelAttribute Task task,
                              @RequestParam("subProjectId") int subProjectId,
                              HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        taskService.editTask(task);
        return "redirect:/task/list/" + subProjectId;
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
