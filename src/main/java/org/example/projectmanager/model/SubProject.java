package org.example.projectmanager.model;

public class SubProject {
    private int subProjectID;
    private String name;
    private String description;
    private int estimatedTime;
    private int actualTime;

    public SubProject(int subProjectID, String name, String description, int estimatedTime, int actualTime) {
        this.subProjectID = subProjectID;
        this.name = name;
        this.description = description;
        this.estimatedTime = estimatedTime;
        this.actualTime = actualTime;

    }
    public SubProject(){}

    public int getSubProjectID() {
        return subProjectID;
    }

    public void setSubProjectID(int subProjectID) {
        this.subProjectID = subProjectID;
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

    public int getActualTime() {
        return actualTime;
    }

    public void setActualTime(int actualTime) {
        this.actualTime = actualTime;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
}
