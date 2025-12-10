package org.example.projectmanager.model;

public class Team {
    private int teamId;
    private String teamName;
    private String teamDescription;
    private int projectId;
    private int subProjectId;
    private int taskId;

    // Full constructor (used by RowMapper)
    public Team(int teamId, String teamName, String teamDescription, int projectId, int subProjectId, int taskId) {
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
    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
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

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getSubProjectId() {
        return subProjectId;
    }

    public void setSubProjectId(int subProjectId) {
        this.subProjectId = subProjectId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
