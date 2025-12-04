package org.example.projectmanager.model;

public class Subtask {
    private int subTaskId;
    private String subTaskName;
    private String subTaskDescription;
    private int subTaskTime;
    private int taskId;  // Foreign key to TASK

    // Full constructor (used by RowMapper)
    public Subtask(int subTaskId, String subTaskName, String subTaskDescription,
                   int subTaskTime, int taskId) {
        this.subTaskId = subTaskId;
        this.subTaskName = subTaskName;
        this.subTaskDescription = subTaskDescription;
        this.subTaskTime = subTaskTime;
        this.taskId = taskId;
    }

    // Empty constructor (used by forms)
    public Subtask() {}

    // Getters and Setters
    public int getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(int subTaskId) {
        this.subTaskId = subTaskId;
    }

    public String getSubTaskName() {
        return subTaskName;
    }

    public void setSubTaskName(String subTaskName) {
        this.subTaskName = subTaskName;
    }

    public String getSubTaskDescription() {
        return subTaskDescription;
    }

    public void setSubTaskDescription(String subTaskDescription) {
        this.subTaskDescription = subTaskDescription;
    }

    public int getSubTaskTime() {
        return subTaskTime;
    }

    public void setSubTaskTime(int subTaskTime) {
        this.subTaskTime = subTaskTime;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
