package org.example.projectmanager.model;

import java.time.LocalDate;

public class Project {

    int projectId;
    String name;
    String description;
    String status;
    String priority;
    int estimatedTime;
    int actualTime;
    LocalDate startDate;
    LocalDate endDate;

    public Project(int projectId, String name, String description, String status, String priority, int estimatedTime, int actualTime, LocalDate startDate, LocalDate endDate){
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.estimatedTime = estimatedTime;
        this.actualTime = actualTime;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Project(){}




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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
