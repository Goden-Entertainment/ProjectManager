package org.example.projectmanager.service;

import org.example.projectmanager.model.Task;
import org.example.projectmanager.model.Team;
import org.example.projectmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
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
        return taskRepository.getTasksBySubProjectId(subProjectId);
    }

    public int getSubProjectIdByTaskId(int taskId) {
        return taskRepository.getSubProjectIdByTaskId(taskId);
    }

    public Team getTeamByTaskId(int taskId) {
        return taskRepository.getTeamByTaskId(taskId);
    }
}
