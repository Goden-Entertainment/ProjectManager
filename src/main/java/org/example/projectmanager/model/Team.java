package org.example.projectmanager.model;

public class Team {
    private Integer teamId;
    private String teamName;
    private String teamDescription;
    private Integer projectId;
    private Integer subProjectId;
    private Integer taskId;

    // Full constructor (used by RowMapper)
    public Team(Integer teamId, String teamName, String teamDescription, Integer projectId, Integer subProjectId, Integer taskId) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamDescription = teamDescription;
        this.projectId = projectId;
        this.subProjectId = subProjectId;
        this.taskId = taskId;
    }

    // Empty constructor (used by forms)
    public Team() {}

    // Getters and Setters
    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamDescription() {
        return teamDescription;
    }

    public void setTeamDescription(String teamDescription) {
        this.teamDescription = teamDescription;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getSubProjectId() {
        return subProjectId;
    }

    public void setSubProjectId(Integer subProjectId) {
        this.subProjectId = subProjectId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }
}
