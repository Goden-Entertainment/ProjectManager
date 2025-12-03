package org.example.projectmanager.h2Test;


import org.example.projectmanager.controller.UserController;
import org.example.projectmanager.model.User;
import org.example.projectmanager.model.userType;
import org.example.projectmanager.repository.UserRepository;
import org.example.projectmanager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "classpath:h2init.sql", executionPhase = BEFORE_TEST_METHOD)
public class ProjectMangerAppTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository repo;

    @Autowired
    private UserService service;

    @Autowired
    private UserController controller;

    @Test
    void insertUserReadBack() {

        var ADMIN = repo.findUser("ADMIN");
        var PM = repo.findUser("PM");
        var DEV1 = repo.findUser("DEV1");

        assertThat(ADMIN).isNotNull();
        assertThat(PM).isNotNull();
        assertThat(DEV1).isNotNull();

        assertThat(ADMIN.getUsername()).isEqualTo("ADMIN");
        assertThat(PM.getUsername()).isEqualTo("PM");
        assertThat(DEV1.getUsername()).isEqualTo("DEV1");
    }

    @Test
    void
    loginSetsUserInSessionWhenCredentialsAreCorrect()
            throws Exception {
        // Perform login med korrekte credentials
        MvcResult result =
                mockMvc.perform(post("/user/login")
                                .param("username", "ADMIN")
                                .param("password",
                                        "admin123"))

                        .andExpect(status().is3xxRedirection())

                        .andExpect(redirectedUrl("/user/profile"))
                        .andReturn();
        // <-- Vigtigt! Få resultat tilbage

        // Hent session fra resultatet
        MockHttpSession session = (MockHttpSession)
                result.getRequest().getSession();
        User user = (User)
                session.getAttribute("user");

        // Assert at user er sat i session
        assertThat(user).isNotNull();

        assertThat(user.getUsername()).isEqualTo("ADMIN");
        assertThat(user.getUserType()).isEqualTo(userType.ADMIN);
    }

    @Test
    void
    loginDoesNotSetUserInSessionWhenPasswordIsWrong() throws Exception {
        // Perform login med forkert password
        MvcResult result = mockMvc.perform(post("/user/login")
                                .param("username", "ADMIN")
                                .param("password", "wrongpassword"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/user/login?error=true"))
                                .andReturn();

        // Hent session
        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();
        User user = (User) session.getAttribute("user");

        // Assert at user IKKE er sat
        assertThat(user).isNull();
    }

    @Test
    void
    loginDoesNotSetUserInSessionWhenUserDoesNotExist()throws Exception {
        MvcResult result = mockMvc.perform(post("/user/login")
                        .param("username", "NONEXISTENT")
                        .param("password", "123"))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/user/login?error=true"))
                        .andReturn();

        MockHttpSession session = (MockHttpSession)result.getRequest().getSession();
        User user = (User)session.getAttribute("user");

        assertThat(user).isNull();
    }
}
