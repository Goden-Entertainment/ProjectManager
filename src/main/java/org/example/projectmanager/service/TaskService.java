package org.example.projectmanager.service;

import jakarta.servlet.http.HttpSession;
import org.example.projectmanager.model.SubProject;
import org.example.projectmanager.model.Task;
import org.example.projectmanager.model.Team;
import org.example.projectmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final SubtaskService subtaskService;
    TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository, SubtaskService subtaskService) {
        this.taskRepository = taskRepository;
        this.subtaskService = subtaskService;
    }

    public int createTask(Task task) {
        return taskRepository.createTask(task);
    }

    public List<Task> getTasks() {
        return taskRepository.getTasks();
    }

    public Task findTask(int taskId) {
        return taskRepository.findTask(taskId);
    }

    public void editTask(Task task) {
        taskRepository.editTask(task);
    }

    public int deleteTask(int taskId) {
        return taskRepository.deleteTask(taskId);
    }

    public List<Task> getTasksBySubProjectId(int subProjectId) {
        List<Task> taskList = taskRepository.getTasksBySubProjectId(subProjectId);

                for(Task task : taskList){
                    int totalActualTime = subtaskService.getTotalActualTime(task.getTaskId());
                    task.setActualTime(totalActualTime);

                    editTask(task);
                }

        return taskList;
    }

    public int getTotalActualTime(int subProjectId){
        int totalTime = 0;

        List<Task> taskList = taskRepository.getTasksBySubProjectId(subProjectId);
        for(Task task : taskList){
            totalTime += task.getActualTime();
        }

        return totalTime;
    }

    public int getSubProjectIdByTaskId(int taskId) {
        return taskRepository.getSubProjectIdByTaskId(taskId);
    }
}
