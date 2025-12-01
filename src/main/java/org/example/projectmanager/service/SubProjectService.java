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

    // Junction table methods
    public void assignSubProjectToProject(int subProjectId, int projectId) {
        subProjectRepository.assignSubProjectToProject(subProjectId, projectId);
    }

    public void removeSubProjectFromProject(int subProjectId, int projectId) {
        subProjectRepository.removeSubProjectFromProject(subProjectId, projectId);
    }

    public List<Integer> getProjectIdsBySubProjectId(int subProjectId) {
        return subProjectRepository.getProjectIdsBySubProjectId(subProjectId);
    }
}
