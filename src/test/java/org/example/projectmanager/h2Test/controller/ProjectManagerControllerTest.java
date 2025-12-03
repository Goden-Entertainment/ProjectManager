package org.example.projectmanager.h2Test.controller;


import org.example.projectmanager.controller.UserController;
import org.example.projectmanager.model.User;
import org.example.projectmanager.model.devType;
import org.example.projectmanager.model.userType;
import org.example.projectmanager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
public class ProjectManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    //Tester profile endpointet, hvor admin blir lavet og prøver at få en liste ud.
    @Test
    void shouldGetAllUsersViaAdmin() throws Exception {

        User adminUser = new User(
                1,
                "admin1",
                "admin123",
                "admin@gmail.com",
                userType.ADMIN,
                devType.FULLSTACK,
                18
        );
    User testUser = new User(1, "Goden", "kode", "goden@gmail.com", userType.DEV, devType.FULLSTACK, 18);


    when(userService.getUsers()).thenReturn(List.of(testUser));

        mockMvc.perform(get("/user/profile")
                        .sessionAttr("user", adminUser))
                .andExpect(status().isOk())
                .andExpect(view().name("adminProfile"))
                .andExpect(model().attributeExists("userList"))
                .andExpect(model().attribute("userList", List.of(testUser)));
    }

    //Tester editUser Get endpoint.
    @Test
    void shouldEditUser() throws Exception{
        User testUser = new User (1, "Yadi", "kode123", "yadi@gmail.com", userType.DEV, devType.BACKEND, 15);

        when(userService.findUser(1)).thenReturn(testUser);

        mockMvc.perform(get("/user/editUser/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("editUserForm"))
                .andExpect(model().attribute("user", testUser))
                .andExpect(model().attributeExists("userTypeEnums"))
                .andExpect(model().attributeExists("devTypeEnums"));

    }

    //Tester deleteUser endpoint.
    @Test
    void shouldDeleteUser() throws Exception{
        User testUser = new User (1, "Yadi", "kode123", "yadi@gmail.com", userType.DEV, devType.BACKEND, 15);

        doNothing().when(userService).deleteUser(1);

        mockMvc.perform(get("/user/deleteUser/{userId}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/profile"));

        verify(userService, times(1)).deleteUser(1);
    }

    @Test
    void shouldLoginIfSessionIsInactive() throws Exception {
        mockMvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void shouldLoginIfSessionIsActive() throws Exception{
        User testUser = new User (1, "Yadi", "kode123", "yadi@gmail.com", userType.PROJECTMANAGER, devType.BACKEND, 15);

        mockMvc.perform(get("/user/login")
                .sessionAttr("user", testUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/profile"));
    }

    @Test
    void shouldLoginIfInfoIsCorrect() throws Exception{
        User testUser = new User (1, "yadi", "kode123", "yadi@gmail.com", userType.PROJECTMANAGER, devType.BACKEND, 15);

        when(userService.login("yadi", "kode123")).thenReturn(testUser);

        mockMvc.perform(post("/user/login")
                .param("username", "yadi")
                .param("password", "kode123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/profile"))
                .andExpect(request().sessionAttribute("user", testUser));

        verify(userService, times(1)).login("yadi", "kode123");

    }

    @Test
    void shouldNotLoginIfInfoIsWrong()throws Exception{

        when(userService.login("wrong", "wrongagain")).thenReturn(null);

        mockMvc.perform(post("/user/login")
                .param("username", "wrong")
                .param("password", "wrongagain"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login"));

        verify(userService, times(1)).login("wrong", "wrongagain");
    }

}
