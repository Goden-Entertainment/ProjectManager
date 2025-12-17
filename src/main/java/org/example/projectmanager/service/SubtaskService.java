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

    public Subtask findSubtask(int subtaskId) {
        return subtaskRepository.findSubtask(subtaskId);
    }

    public void editSubtask(Subtask subtask) {
        subtaskRepository.editSubtask(subtask);
    }

    public void deleteSubtask(int subtaskId) {
        subtaskRepository.deleteSubtask(subtaskId);
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

    public int getTaskIdBySubtaskId(int subtaskId) {
        return subtaskRepository.getTaskIdBySubtaskId(subtaskId);
    }

    // Junction table methods
    public void assignDevToSubtask(int devId, int subtaskId) {
        subtaskRepository.assignDevToSubtask(devId, subtaskId);
    }

    public List<Integer> getCurrentlyAssignedDevIds(int subtaskId) {
        return subtaskRepository.getCurrentlyAssignedDevIds(subtaskId);
    }

    public List<User> getDevsBySubtaskId(int subtaskId) {
        return subtaskRepository.getDevsBySubtaskId(subtaskId);
    }

    public void removeAllDevsFromSubtask(int subtaskId) {
        subtaskRepository.removeAllDevsFromSubtask(subtaskId);
    }


}
