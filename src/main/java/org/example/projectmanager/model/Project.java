package org.example.projectmanager.model;

public class Project {

    int projectId;
    String name;
    String description;
    int estimatedTime;
    int actualTime;

    public Project(int projectId, String name, String description, int estimatedTime, int actualTime){
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.estimatedTime = estimatedTime;
        this.actualTime = actualTime;
    }



    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public int getActualTime() {
        return actualTime;
    }

    public void setActualTime(int actualTime) {
        this.actualTime = actualTime;
    }
}
