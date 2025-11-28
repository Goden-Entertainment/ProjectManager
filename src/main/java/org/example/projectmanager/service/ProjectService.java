package org.example.projectmanager.service;

import org.example.projectmanager.model.Project;
import org.example.projectmanager.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    ProjectRepository projectRepository;
    public ProjectService(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    public Project createProject(Project project){
        return projectRepository.createProject(project);
    }

    public List<Project> getProjects(){
        return projectRepository.getProjects();
    }
}
