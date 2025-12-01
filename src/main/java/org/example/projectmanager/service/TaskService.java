package org.example.projectmanager.service;

import org.example.projectmanager.model.Task;
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

    // Junction table methods
    public void assignTaskToSubProject(int taskId, int subProjectId) {
        taskRepository.assignTaskToSubProject(taskId, subProjectId);
    }

    public void removeTaskFromSubProject(int taskId, int subProjectId) {
        taskRepository.removeTaskFromSubProject(taskId, subProjectId);
    }

    public List<Integer> getSubProjectIdsByTaskId(int taskId) {
        return taskRepository.getSubProjectIdsByTaskId(taskId);
    }
}
