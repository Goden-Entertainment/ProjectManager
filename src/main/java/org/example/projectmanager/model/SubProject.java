package org.example.projectmanager.model;

import java.time.LocalDate;

public class SubProject {
    private Integer subProjectId;
    private String name;
    private String description;
    private String status;
    private int estimatedTime;
    private int actualTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private int projectId;  // Foreign key to PROJECT

    // Full constructor (used by RowMapper)
    public SubProject(Integer subProjectId, String name, String description, String status,
                      int estimatedTime, int actualTime, LocalDate startDate, LocalDate endDate, int projectId) {
        this.subProjectId = subProjectId;
        this.name = name;
        this.description = description;
        this.status = status;
        this.estimatedTime = estimatedTime;
        this.actualTime = actualTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.projectId = projectId;
    }

    // Empty constructor (used by forms)
    public SubProject() {
    }

    // Getters and Setters
    public Integer getSubProjectId() {
        return subProjectId;
    }

    public void setSubProjectId(Integer subProjectId) {
        this.subProjectId = subProjectId;
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

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
