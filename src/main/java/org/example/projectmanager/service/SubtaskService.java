package org.example.projectmanager.service;

import org.example.projectmanager.model.Subtask;
import org.example.projectmanager.model.User;
import org.example.projectmanager.repository.SubtaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubtaskService {

    private SubtaskRepository subtaskRepository;

    public SubtaskService(SubtaskRepository subtaskRepository) {
        this.subtaskRepository = subtaskRepository;
    }

    public int createSubtask(Subtask subtask) {
        return subtaskRepository.createSubtask(subtask);
    }

    public List<Subtask> getSubtasks() {
        return subtaskRepository.getSubtasks();
    }

    public Subtask findSubtask(int subTaskId) {
        return subtaskRepository.findSubtask(subTaskId);
    }

    public void editSubtask(Subtask subtask) {
        subtaskRepository.editSubtask(subtask);
    }

    public int deleteSubtask(int subTaskId) {
        return subtaskRepository.deleteSubtask(subTaskId);
    }

    public List<Subtask> getSubtasksByTaskId(int taskId) {
        return subtaskRepository.getSubtasksByTaskId(taskId);
    }

    public int getTaskIdBySubtaskId(int subTaskId) {
        return subtaskRepository.getTaskIdBySubtaskId(subTaskId);
    }

    // Junction table methods
    public void assignUserToSubtask(int userId, int subTaskId) {
        subtaskRepository.assignUserToSubtask(userId, subTaskId);
    }

    public void removeUserFromSubtask(int userId, int subTaskId) {
        subtaskRepository.removeUserFromSubtask(userId, subTaskId);
    }

    public List<User> getUsersBySubtaskId(int subTaskId) {
        return subtaskRepository.getUsersBySubtaskId(subTaskId);
    }

    public void removeAllUsersFromSubtask(int subTaskId) {
        subtaskRepository.removeAllUsersFromSubtask(subTaskId);
    }
}
