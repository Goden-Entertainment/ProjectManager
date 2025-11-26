package org.example.projectmanager.model;

public class User {
    int userId;
    String username;
    String password;
    String email;
    userType userType;
    devType devType;
    int workTime;

    public User(int userId, String userName, String password, String email, userType userType, devType devType, int workTime){
        this.username = userName;
        this.password = password;
        this.email = email;
        this.userType = userType;
        this.devType = devType;
        this.workTime = workTime;
    }

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
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public Enum getDevType() {
        return devType;
    }
    public void setDevType(devType devType) {
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

}
