package org.example.projectmanager.model;


public class User {
    private Integer userId;
    private String username;
    private String password;
    private String email;
    private userType userType;
    private devType devType;
    private int workTime;
    private Integer teamId;


    public User(Integer userId, String userName, String password, String email, userType userType, devType devType, int workTime, Integer teamId){
        this.userId = userId;
        this.username = userName;
        this.password = password;
        this.email = email;
        this.userType = userType;
        this.devType = devType;
        this.workTime = workTime;
        this.teamId = teamId;
    }
    public User(){}


    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Enum getDevType() {
        return devType;
    }
    public void setDevType(devType devType) {
        this.devType = devType;
    }
    public Enum getUserType() {
        return userType;
    }
    public void setUserType(userType userType) {
        this.userType = userType;
    }
    public int getWorkTime() {
        return workTime;
    }
    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }
    public Integer getTeamId() {
        return teamId;
    }
    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }
}
