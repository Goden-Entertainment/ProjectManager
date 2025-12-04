package org.example.projectmanager.controller;

import jakarta.servlet.http.HttpSession;
import org.example.projectmanager.model.*;
import org.example.projectmanager.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("subtask")
public class SubtaskController {

    private SubtaskService subtaskService;
    private TaskService taskService;
    private TeamService teamService;

    public SubtaskController(SubtaskService subtaskService, TaskService taskService, TeamService teamService) {
        this.subtaskService = subtaskService;
        this.taskService = taskService;
        this.teamService = teamService;
    }

    // LIST - Show all subtasks for a task
    @GetMapping("/list/{taskId}")
    public String listSubtasks(@PathVariable int taskId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        // Get task info
        Task task = taskService.findTask(taskId);
        int subProjectId = taskService.getSubProjectIdByTaskId(taskId);

        // Get all subtasks for this task
        List<Subtask> subtasks = subtaskService.getSubtasksByTaskId(taskId);

        // Get assigned users for each subtask
        Map<Integer, List<User>> subtaskUsers = new HashMap<>();
        for (Subtask subtask : subtasks) {
            List<User> assignedUsers = subtaskService.getUsersBySubtaskId(subtask.getSubTaskId());
            subtaskUsers.put(subtask.getSubTaskId(), assignedUsers);
        }

        model.addAttribute("subtasks", subtasks);
        model.addAttribute("subtaskUsers", subtaskUsers);
        model.addAttribute("taskName", task.getName());
        model.addAttribute("taskId", taskId);
        model.addAttribute("subProjectId", subProjectId);

        return "subtasks";
    }

    // ADD - Show form
    @GetMapping("/add/{taskId}")
    public String addSubtask(@PathVariable int taskId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        // Get task info
        Task task = taskService.findTask(taskId);

        // Get team members from task's assigned team
        List<User> availableUsers = new ArrayList<>();
        String teamName = "No Team";
        if (task.getTeamId() != 0) {
            availableUsers = teamService.getUsersByTeamId(task.getTeamId());
            Team team = teamService.findTeam(task.getTeamId());
            if (team != null) {
                teamName = team.getTeamName();
            }
        }

        Subtask newSubtask = new Subtask();
        newSubtask.setTaskId(taskId);

        model.addAttribute("newSubtask", newSubtask);
        model.addAttribute("taskName", task.getName());
        model.addAttribute("teamName", teamName);
        model.addAttribute("availableUsers", availableUsers);
        model.addAttribute("taskId", taskId);

        return "addSubtaskForm";
    }

    // ADD - Process form
    @PostMapping("/add")
    public String createSubtask(@ModelAttribute Subtask subtask,
                                @RequestParam(value = "assignedUsers", required = false) List<Integer> assignedUserIds,
                                HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        // Create subtask
        int subTaskId = subtaskService.createSubtask(subtask);

        // Assign selected users
        if (assignedUserIds != null && !assignedUserIds.isEmpty()) {
            for (int userId : assignedUserIds) {
                subtaskService.assignUserToSubtask(userId, subTaskId);
            }
        }

        return "redirect:/subtask/list/" + subtask.getTaskId();
    }

    // EDIT - Show form
    @GetMapping("/edit/{subTaskId}")
    public String editSubtask(@PathVariable int subTaskId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        // Get subtask
        Subtask subtask = subtaskService.findSubtask(subTaskId);
        int taskId = subtask.getTaskId();
        Task task = taskService.findTask(taskId);

        // Get currently assigned users
        List<User> currentlyAssigned = subtaskService.getUsersBySubtaskId(subTaskId);
        Set<Integer> assignedUserIds = new HashSet<>();
        for (User u : currentlyAssigned) {
            assignedUserIds.add(u.getUserId());
        }

        // Get available users from task's team
        List<User> availableUsers = new ArrayList<>();
        String teamName = "No Team";
        if (task.getTeamId() != 0) {
            availableUsers = teamService.getUsersByTeamId(task.getTeamId());
            Team team = teamService.findTeam(task.getTeamId());
            if (team != null) {
                teamName = team.getTeamName();
            }
        }

        model.addAttribute("subtask", subtask);
        model.addAttribute("taskName", task.getName());
        model.addAttribute("teamName", teamName);
        model.addAttribute("availableUsers", availableUsers);
        model.addAttribute("assignedUserIds", assignedUserIds);
        model.addAttribute("taskId", taskId);

        return "editSubtaskForm";
    }

    // EDIT - Process form
    @PostMapping("/edit")
    public String updateSubtask(@ModelAttribute Subtask subtask,
                                @RequestParam(value = "assignedUsers", required = false) List<Integer> assignedUserIds,
                                HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        // Update subtask
        subtaskService.editSubtask(subtask);

        // Remove all old user assignments
        subtaskService.removeAllUsersFromSubtask(subtask.getSubTaskId());

        // Add new assignments
        if (assignedUserIds != null && !assignedUserIds.isEmpty()) {
            for (int userId : assignedUserIds) {
                subtaskService.assignUserToSubtask(userId, subtask.getSubTaskId());
            }
        }

        return "redirect:/subtask/list/" + subtask.getTaskId();
    }

    // DELETE
    @GetMapping("/delete/{subTaskId}")
    public String deleteSubtask(@PathVariable int subTaskId, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType() != userType.PROJECTMANAGER) {
            return "redirect:/user/profile";
        }

        int taskId = subtaskService.getTaskIdBySubtaskId(subTaskId);
        subtaskService.deleteSubtask(subTaskId);

        return "redirect:/subtask/list/" + taskId;
    }
}
