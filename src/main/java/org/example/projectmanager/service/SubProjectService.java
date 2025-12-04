package org.example.projectmanager.service;

import org.example.projectmanager.model.SubProject;
import org.example.projectmanager.repository.SubProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubProjectService {
    private final TaskService taskService;
    SubProjectRepository subProjectRepository;

    public SubProjectService(SubProjectRepository subProjectRepository, TaskService taskService) {
        this.subProjectRepository = subProjectRepository;
        this.taskService = taskService;
    }

    public int createSubProject(SubProject subProject) {
        return subProjectRepository.createSubProject(subProject);
    }

    public SubProject findSubProject(int subProjectId) {
        return subProjectRepository.findSubProject(subProjectId);
    }

    public void editSubProject(SubProject subProject) {
        subProjectRepository.editSubProject(subProject);
    }

    public int deleteSubProject(int subProjectId) {
        return subProjectRepository.deleteSubProject(subProjectId);
    }

    public List<SubProject> getSubProjectsByProjectId(int projectId) {
        List<SubProject> subProjectList = subProjectRepository.getSubProjectsByProjectId(projectId);

        for(SubProject subProject : subProjectList){
            int totalActualTime = taskService.getTotalActualTime(subProject.getSubProjectId());
            subProject.setActualTime(totalActualTime);

            editSubProject(subProject);
        }

        return subProjectList;
    }

    public int getTotalActualTime(int projectId){
        int totalTime = 0;

        List<SubProject> subProjectList = subProjectRepository.getSubProjectsByProjectId(projectId);
        for(SubProject subProject : subProjectList){
            totalTime += subProject.getActualTime();
        }

        return totalTime;
    }

    public int getProjectIdBySubProjectId(int subProjectId) {
        return subProjectRepository.getProjectIdBySubProjectId(subProjectId);
    }
}
