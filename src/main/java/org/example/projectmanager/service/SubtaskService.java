package org.example.projectmanager.service;

import jakarta.servlet.http.HttpSession;
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

    public int getTotalActualTime(int taskId){
        int totalTime = 0;

        List<Subtask> subtaskList = subtaskRepository.getSubtasksByTaskId(taskId);
        for(Subtask subtask : subtaskList){
            totalTime += subtask.getActualTime();
        }

        return totalTime;
    }

    public int getTaskIdBySubtaskId(int subTaskId) {
        return subtaskRepository.getTaskIdBySubtaskId(subTaskId);
    }

    // Junction table methods
    public void assignDevToSubtask(int devId, int subTaskId) {
        subtaskRepository.assignDevToSubtask(devId, subTaskId);
    }

    public void removeDevFromSubtask(int devId, int subTaskId) {
        subtaskRepository.removeDevFromSubtask(devId, subTaskId);
    }

    public List<Integer> getCurrentlyAssignedDevIds(int subTaskId) {
        return subtaskRepository.getCurrentlyAssignedDevIds(subTaskId);
    }

    public List<User> getDevsBySubtaskId(int subTaskId) {
        return subtaskRepository.getDevsBySubtaskId(subTaskId);
    }

    public void removeAllUsersFromSubtask(int subTaskId) {
        subtaskRepository.removeAllUsersFromSubtask(subTaskId);
    }


}
