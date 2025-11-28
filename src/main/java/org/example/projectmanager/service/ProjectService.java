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

    public int createProject(Project project){
        return projectRepository.createProject(project);
    }

    public List<Project> getProjects(){
        return projectRepository.getProjects();
    }

    public Project findProject(int projectId){
        return projectRepository.findProject(projectId);
    }

    public void editProject(Project project){
        projectRepository.editProject(project);
    }

    public int deleteProject(int projectId){
        return projectRepository.deleteProject(projectId);
    }

    public List<Project> getProjectsByUserId(int userId){
        return projectRepository.getProjectsByUserId(userId);
    }

    public void assignProjectToUser(int projectId, int userId){
        projectRepository.assignProjectToUser(projectId, userId);
    }
}
