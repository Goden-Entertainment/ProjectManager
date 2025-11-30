package org.example.projectmanager.service;

import org.example.projectmanager.model.SubProject;
import org.example.projectmanager.repository.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    ProjectRepository projectRepository;

    public ProjectService (ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    public SubProject createSubProject(SubProject subProject){
       return projectRepository.createSubProject(subProject);
    }

    public void editSubProject(SubProject subProject){
        projectRepository.editSubProject(subProject);
    }

    public SubProject findSubProject(int subProjectId) {
        return projectRepository.findSubProject(subProjectId);

    }

    public int deleteSubProject(int subProjectId){
       return projectRepository.deleteSubProject(subProjectId);
    }
}
