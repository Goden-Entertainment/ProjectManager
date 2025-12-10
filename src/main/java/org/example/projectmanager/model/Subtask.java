package org.example.projectmanager.model;

import java.time.LocalDate;

public class Subtask {
    private int subTaskId;
    private String subTaskName;
    private String subTaskDescription;
    private String status;
    private int estimatedTime;
    private int actualTime;
    private String priority;
    private LocalDate startDate;
    private LocalDate endDate;
    private int taskId;  // Foreign key to TASK

    // Full constructor (used by RowMapper)
    public Subtask(int subTaskId, String subTaskName, String subTaskDescription, String status,
                   int estimatedTime, int actualTime, String priority, LocalDate startDate, LocalDate endDate, int taskId) {
        this.subTaskId = subTaskId;
        this.subTaskName = subTaskName;
        this.subTaskDescription = subTaskDescription;
        this.status = status;
        this.estimatedTime = estimatedTime;
        this.actualTime = actualTime;
        this.priority = priority;
        this.startDate = startDate;
        this.endDate = endDate;
        this.taskId = taskId;
    }

    // Empty constructor (used by forms)
    public Subtask() {}

    // Getters and Setters
    public int getActualTime(){
        return actualTime;
    }
    public void setActualTime(int actualTime){
        this.actualTime = actualTime;
    }
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

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
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
