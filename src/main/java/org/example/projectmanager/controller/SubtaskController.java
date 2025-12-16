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

    private final UserService userService;
    private final SubtaskService subtaskService;
    private final TaskService taskService;

    public SubtaskController(SubtaskService subtaskService, TaskService taskService, UserService userService) {
        this.subtaskService = subtaskService;
        this.taskService = taskService;
        this.userService = userService;
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
        Map<Integer, List<User>> subtaskDevs = new HashMap<>();
        for (Subtask subtask : subtasks) {
            List<User> assignedUsers = subtaskService.getDevsBySubtaskId(subtask.getSubTaskId());
            subtaskDevs.put(subtask.getSubTaskId(), assignedUsers);
        }

        int totalActualTime = subtaskService.getTotalActualTime(taskId);

        model.addAttribute("totalActualTime", totalActualTime);
        model.addAttribute("subtasks", subtasks);
        model.addAttribute("subtaskDevs", subtaskDevs);
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

        List<User> availableDevs = userService.allAvailableDevsFor_SubTask(taskId);

        Subtask newSubtask = new Subtask();
        newSubtask.setTaskId(taskId);

        model.addAttribute("availableDevs", availableDevs);
        model.addAttribute("newSubtask", newSubtask);
        model.addAttribute("taskName", task.getName());
        model.addAttribute("taskId", taskId);

        return "addSubtaskForm";
    }

    // ADD - Process form
    @PostMapping("/add")
    public String createSubtask(@ModelAttribute Subtask subtask,
                                @RequestParam(required = false) List<Integer> selectedDevIds,
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
        if (selectedDevIds != null && !selectedDevIds.isEmpty()) {
            for (int devId : selectedDevIds) {
                subtaskService.assignDevToSubtask(devId, subTaskId);
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

        // Get currently assigned developer id's
        List<Integer> currentlyAssignedDevIds = subtaskService.getCurrentlyAssignedDevIds(subTaskId);

        //AVAILABLE DEVS
        List<User> availableDevs = userService.allAvailableDevsFor_SubTask(subtask);


        model.addAttribute("subtask", subtask);
        model.addAttribute("taskName", task.getName());
        model.addAttribute("availableDevs", availableDevs);
        model.addAttribute("currentlyAssignedDevIds", currentlyAssignedDevIds);
        model.addAttribute("taskId", taskId);

        return "editSubtaskForm";
    }

    // EDIT - Process form
    @PostMapping("/edit")
    public String updateSubtask(@ModelAttribute Subtask subtask,
                                @RequestParam(required = false) List<Integer> selectedDevIds,
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

        if(selectedDevIds != null && !selectedDevIds.isEmpty()){
            // Add new assignments
            for (int devId : selectedDevIds) {
                subtaskService.assignDevToSubtask(devId, subtask.getSubTaskId());
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
