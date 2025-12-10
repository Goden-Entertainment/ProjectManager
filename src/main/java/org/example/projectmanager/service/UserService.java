package org.example.projectmanager.service;

import org.example.projectmanager.exceptions.DatabaseOperationException;
import org.example.projectmanager.exceptions.ProfileNotFoundException;
import org.example.projectmanager.model.User;
import org.example.projectmanager.repository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getUsers(){
        return  userRepository.getUsers();
    }

    public User createUser(User user){
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
        }

        return null;
    }

    public void removeTeamMembers(int teamId){

    }
}