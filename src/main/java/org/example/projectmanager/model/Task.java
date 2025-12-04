package org.example.projectmanager.model;

import java.time.LocalDate;

public class Task {
    private int taskId;
    private String name;
    private String description;
    private int teamId;  // Foreign key to TEAM
    private String status;
    private int estimatedTime;
    private int actualTime;
    private String priority;
    private LocalDate startDate;
    private LocalDate endDate;
    private int subProjectId;  // Foreign key to SUBPROJECT

    // Full constructor (used by RowMapper)
    public Task(int taskId, String name, String description, int teamId, String status,
                int estimatedTime, int actualTime, String priority, LocalDate startDate, LocalDate endDate, int subProjectId) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.teamId = teamId;
        this.status = status;
        this.estimatedTime = estimatedTime;
        this.actualTime = actualTime;
        this.priority = priority;
        this.startDate = startDate;
        this.endDate = endDate;
        this.subProjectId = subProjectId;
    }

    // Empty constructor (used by forms)
    public Task() {
    }

    // Getters and Setters
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
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

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public int getSubProjectId() {
        return subProjectId;
    }

    public void setSubProjectId(int subProjectId) {
        this.subProjectId = subProjectId;
    }
}
