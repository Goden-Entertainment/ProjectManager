package org.example.projectmanager.controller;

import jakarta.servlet.http.HttpSession;
import org.example.projectmanager.model.User;
import org.example.projectmanager.model.devType;
import org.example.projectmanager.model.userType;
import org.example.projectmanager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String profile(Model model) {
        List<User> userList = userService.getUsers();
        model.addAttribute("userList", userList);
        return "adminProfile";
    }

    @GetMapping("/addNewUser")
    public String addNewUser(HttpSession session, Model model) {
        User user = new User();
        model.addAttribute("userTypeEnums", userType.values());
        model.addAttribute("devTypeEnums", devType.values());
        model.addAttribute("newUser", user);

        return "addNewUserForm";
    }

    @PostMapping("/addNewUser")
    public String createUser(@ModelAttribute User user) {
        userService.createUser(user);
        return "redirect:/user/profile";
    }
}





