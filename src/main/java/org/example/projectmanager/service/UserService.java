package org.example.projectmanager.service;

import org.example.projectmanager.exceptions.DatabaseOperationException;
import org.example.projectmanager.exceptions.ProfileNotFoundException;
import org.example.projectmanager.model.Subtask;
import org.example.projectmanager.model.Team;
import org.example.projectmanager.model.User;
import org.example.projectmanager.repository.TeamRepository;
import org.example.projectmanager.repository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class UserService {
    UserRepository userRepository;
    TeamRepository teamRepository;

    public UserService(UserRepository userRepository, TeamRepository teamRepository){
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    public List<User> getUsers(){
        return  userRepository.getUsers();
    }

    public int createUser(User user){
       return userRepository.createUser(user);
    }

    public void editUser(User user){
        userRepository.editUser(user);
    }

    public User findUser(int userId){
        return userRepository.findUser(userId);
    }

    public void deleteUser(int user_id){
        userRepository.deleteUser(user_id);
    }

    public User login(String username, String password) {
        User user = userRepository.findUser(username);

        if (user.getPassword().equals(password)) {

            return user;
        } else if (! user.getPassword().equals(password)){
            throw new ProfileNotFoundException();
        } else{

            return null;
        }
    }

    public List<User> allAvailableDevsFor_SubTask(Subtask subtask) {
        List<Team> availableTeams = teamRepository.allAvailableTeamsFor_SubTask(subtask.getTaskId());
        List<User> availableDevs = new ArrayList<>();

        for(Team team : availableTeams){
            availableDevs.addAll(userRepository.getAvailableTeamDevs(team.getTeamId(), subtask.getSubTaskId()));
        }


        return availableDevs;
    }

    public List<User> allAvailableDevsFor_SubTask(int taskId) {
        List<Team> availableTeams = teamRepository.allAvailableTeamsFor_SubTask(taskId);
        List<User> availableDevs = new ArrayList<>();

        for(Team team : availableTeams){
            availableDevs.addAll(userRepository.getAvailableTeamDevs(team.getTeamId()));
        }


        return availableDevs;
    }
}