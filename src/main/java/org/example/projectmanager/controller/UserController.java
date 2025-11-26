package org.example.projectmanager.controller;

import jakarta.websocket.Session;
import org.example.projectmanager.model.User;
import org.example.projectmanager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {

    private UserService userService;
    private User user;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(Session session, Model model) {
        List<User> userList = userService.getUsers();
        model.addAttribute("userList", userList);
        return "adminProfile";
    }
}


