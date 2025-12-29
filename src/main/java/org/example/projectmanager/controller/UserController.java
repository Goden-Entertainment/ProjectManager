package org.example.projectmanager.controller;

import jakarta.servlet.http.HttpSession;
import org.example.projectmanager.exceptions.DatabaseOperationException;
import org.example.projectmanager.exceptions.ProfileNotFoundException;
import org.example.projectmanager.model.User;
import org.example.projectmanager.model.devType;
import org.example.projectmanager.model.userType;
import org.example.projectmanager.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        //Skaber et User objekt til at holde useren hvis du er logget ind og har started en session
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/user/login";
        }

        if (user.getUserType().equals(userType.valueOf("ADMIN"))) {
            List<User> userList = userService.getUsers();
            model.addAttribute("userList", userList);
            return "adminProfile";
        } else if (user.getUserType().equals(userType.valueOf("PROJECTMANAGER"))) {
            return "pmProfile";
        }
        return "redirect:/user/login";
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

    @GetMapping("/editUser/{userId}")
    public String editUser(@PathVariable int userId, Model model) {
        User user = userService.findUser(userId);
        model.addAttribute("user", user);
        model.addAttribute("userTypeEnums", userType.values());
        model.addAttribute("devTypeEnums", devType.values());
        return "editUserForm";
    }

    @PostMapping("/editUser")
    public String updateUser(@ModelAttribute User user) {
        userService.editUser(user);
        return "redirect:/user/profile";
    }

    @GetMapping("/deleteUser/{userId}")
    public String deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return "redirect:/user/profile";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/user/profile";
        }

        return "login";
    }

    @PostMapping("/login")
    public String authenticateUser(@RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   HttpSession session) {
        try {
            User user = userService.login(username, password);

            if (user != null) {
                session.setAttribute("user", user);
                return "redirect:/user/profile";
            }

        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Database fejl ved autentificering", e);
        }

        return "redirect:/user/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }
}





