package org.example.projectmanager.service;

import org.example.projectmanager.model.SubProject;
import org.example.projectmanager.repository.SubProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubProjectService {
    SubProjectRepository subProjectRepository;

    public SubProjectService(SubProjectRepository subProjectRepository) {
        this.subProjectRepository = subProjectRepository;
    }

    public int createSubProject(SubProject subProject) {
        return subProjectRepository.createSubProject(subProject);
    }

    public List<SubProject> getSubProjects() {
        return subProjectRepository.getSubProjects();
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
        return subProjectRepository.getSubProjectsByProjectId(projectId);
    }

    public int getProjectIdBySubProjectId(int subProjectId) {
        return subProjectRepository.getProjectIdBySubProjectId(subProjectId);
    }
}
