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

    // Test: Admin besøger profil-siden og skal kunne se en liste over alle brugere.
    @Test
    void shouldGetAllUsersViaAdmin() throws Exception {
        User adminUser = new User(1, "admin1", "admin123", "admin@gmail.com", userType.ADMIN, devType.FULLSTACK, 18, null);
        User testUser = new User(1, "Goden", "kode", "goden@gmail.com", userType.DEV, devType.FULLSTACK, 18, null);

        // Mock servicen til at returnere vores test-bruger
        when(userService.getUsers()).thenReturn(List.of(testUser));

        // Udfør GET-request som admin og tjek:
        // - status 200 (OK)
        // - view er "adminProfile"
        // - modellen har attributten "userList" med vores test-bruger
        mockMvc.perform(get("/user/profile")
                        .sessionAttr("user", adminUser))
                .andExpect(status().isOk())
                .andExpect(view().name("adminProfile"))
                .andExpect(model().attributeExists("userList"))
                .andExpect(model().attribute("userList", List.of(testUser)));
    }

    // Test: En bruger eller admin åbner siden for at redigere en bruger.
    @Test
    void shouldEditUser() throws Exception{
        User testUser = new User(1, "Yadi", "kode123", "yadi@gmail.com", userType.DEV, devType.BACKEND, 15, null);

        // Mock servicen til at returnere brugeren som skal redigeres
        when(userService.findUser(1)).thenReturn(testUser);

        // Udfør GET-request for redigering og tjek:
        // - status 200
        // - view er "editUserForm"
        // - modellen indeholder brugeren og enum-lister til dropdowns
        mockMvc.perform(get("/user/editUser/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("editUserForm"))
                .andExpect(model().attribute("user", testUser))
                .andExpect(model().attributeExists("userTypeEnums"))
                .andExpect(model().attributeExists("devTypeEnums"));
    }

    // Test: Sletning af en bruger.
    @Test
    void shouldDeleteUser() throws Exception{
        // Mock servicen til at gøre intet ved sletning (void metode)
        doNothing().when(userService).deleteUser(1);

        // Udfør GET-request for at slette brugeren og tjek:
        // - redirect (3xx) til profil-siden
        mockMvc.perform(get("/user/deleteUser/{userId}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/profile"));

        // Tjek at deleteUser metoden blev kaldt én gang
        verify(userService, times(1)).deleteUser(1);
    }

    // Test: GET /login når sessionen ikke er aktiv.
    @Test
    void shouldLoginIfSessionIsInactive() throws Exception {
        // Udfør GET-request uden session og tjek:
        // - status 200
        // - view er "login"
        mockMvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    // Test: GET /login når sessionen allerede har en bruger
    @Test
    void shouldLoginIfSessionIsActive() throws Exception{
        User testUser = new User(1, "Yadi", "kode123", "yadi@gmail.com", userType.PROJECTMANAGER, devType.BACKEND, 15, null);

        // Udfør GET-request med session og tjek:
        // - redirect (3xx) til profil-siden
        mockMvc.perform(get("/user/login")
                        .sessionAttr("user", testUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/profile"));
    }

    // Test: POST /login med korrekt login-info
    @Test
    void shouldLoginIfInfoIsCorrect() throws Exception{
        User testUser = new User(1, "yadi", "kode123", "yadi@gmail.com", userType.PROJECTMANAGER, devType.BACKEND, 15, null);

        // Mock servicen til at returnere brugeren ved korrekt login
        when(userService.login("yadi", "kode123")).thenReturn(testUser);

        // Udfør POST-request med parametre og tjek:
        // - redirect (3xx) til profil-siden
        // - sessionen indeholder brugeren
        mockMvc.perform(post("/user/login")
                        .param("username", "yadi")
                        .param("password", "kode123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/profile"))
                .andExpect(request().sessionAttribute("user", testUser));

        // Tjek at login-metoden blev kaldt én gang
        verify(userService, times(1)).login("yadi", "kode123");
    }

    // Test: POST /login med forkert login-info
    @Test
    void shouldNotLoginIfInfoIsWrong() throws Exception{
        // Mock servicen til at returnere null ved forkert login
        when(userService.login("wrong", "wrongagain")).thenReturn(null);

        // Udfør POST-request og tjek:
        // - redirect (3xx) tilbage til login-siden
        mockMvc.perform(post("/user/login")
                        .param("username", "wrong")
                        .param("password", "wrongagain"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login"));

        // Tjek at login-metoden blev kaldt én gang med de forkerte oplysninger
        verify(userService, times(1)).login("wrong", "wrongagain");
    }

}
