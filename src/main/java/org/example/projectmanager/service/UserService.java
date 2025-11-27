package org.example.projectmanager.service;

import org.example.projectmanager.model.User;
import org.example.projectmanager.repository.UserRepository;
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

}

