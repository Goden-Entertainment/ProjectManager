package org.example.projectmanager.model;

import java.time.LocalDate;

public class SubProject {
    private int subProjectId;
    private String name;
    private String description;
    private String team;
    private String status;
    private int estimatedTime;
    private int actualTime;
    private LocalDate startDate;
    private LocalDate endDate;

    // Full constructor (used by RowMapper)
    public SubProject(int subProjectId, String name, String description, String team, String status,
                      int estimatedTime, int actualTime, LocalDate startDate, LocalDate endDate) {
        this.subProjectId = subProjectId;
        this.name = name;
        this.description = description;
        this.team = team;
        this.status = status;
        this.estimatedTime = estimatedTime;
        this.actualTime = actualTime;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Empty constructor (used by forms)
    public SubProject() {
    }

    // Getters and Setters
    public int getSubProjectId() {
        return subProjectId;
    }

    public void setSubProjectId(int subProjectId) {
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

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
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
}
