package org.example.projectmanager.controller;

import jakarta.servlet.http.HttpSession;
import org.example.projectmanager.model.SubProject;
import org.example.projectmanager.model.Task;
import org.example.projectmanager.model.User;
import org.example.projectmanager.model.userType;
import org.example.projectmanager.service.SubProjectService;
import org.example.projectmanager.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("task")
public class TaskController {

    private TaskService taskService;
    private SubProjectService subProjectService;

    public TaskController(TaskService taskService, SubProjectService subProjectService) {
        this.taskService = taskService;
        this.subProjectService = subProjectService;
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

        // Get project ID for back button
        List<Integer> projectIds = subProjectService.getProjectIdsBySubProjectId(subProjectId);
        int projectId = projectIds.isEmpty() ? 0 : projectIds.get(0);

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

        model.addAttribute("newTask", new Task());
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

        // Create task first
        int taskId = taskService.createTask(task);

        // Then assign it to the subproject via junction table
        taskService.assignTaskToSubProject(taskId, subProjectId);

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

        // Get the first subproject this task belongs to (for the redirect)
        List<Integer> subProjectIds = taskService.getSubProjectIdsByTaskId(taskId);
        int subProjectId = subProjectIds.isEmpty() ? 0 : subProjectIds.get(0);

        model.addAttribute("task", task);
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

        // Get subproject ID before deleting (for redirect)
        List<Integer> subProjectIds = taskService.getSubProjectIdsByTaskId(taskId);
        int subProjectId = subProjectIds.isEmpty() ? 0 : subProjectIds.get(0);

        taskService.deleteTask(taskId);
        return "redirect:/task/list/" + subProjectId;
    }
}
