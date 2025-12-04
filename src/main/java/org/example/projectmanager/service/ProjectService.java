package org.example.projectmanager.service;

import org.example.projectmanager.model.Project;
import org.example.projectmanager.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final SubProjectService subProjectService;
    ProjectRepository projectRepository;
    public ProjectService(ProjectRepository projectRepository, SubProjectService subProjectService){
        this.projectRepository = projectRepository;
        this.subProjectService = subProjectService;
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
        List<Project> projectList = projectRepository.getProjectsByUserId(userId);

        for(Project project : projectList){
            int totalActualTime = subProjectService.getTotalActualTime(project.getProjectId());

            project.setActualTime(totalActualTime);
            projectRepository.editProject(project);
        }

        return projectList;
    }

    public void assignProjectToUser(int projectId, int userId){
        projectRepository.assignProjectToUser(projectId, userId);
    }
}
