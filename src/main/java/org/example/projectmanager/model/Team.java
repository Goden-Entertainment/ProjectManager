package org.example.projectmanager.model;

public class Team {
    private int teamId;
    private String teamName;
    private String teamDescription;

    // Full constructor (used by RowMapper)
    public Team(int teamId, String teamName, String teamDescription) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamDescription = teamDescription;
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
}
