package org.example.projectmanager.h2Test;


import org.example.projectmanager.model.User;
import org.example.projectmanager.model.devType;
import org.example.projectmanager.model.userType;
import org.example.projectmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:h2init.sql", executionPhase = BEFORE_TEST_METHOD)
public class ProjectMangerAppTest {

    @Autowired
    private UserRepository repo;

    @Test
    void insertAndReadBack() {

        repo.createUser(new User(1, "DEV1", "123", "DEV1@gmail.com", userType.DEV, devType.FULLSTACK, 20));
        var DEV1 =repo.findUser("DEV1");
        assertThat(DEV1).isNotNull();
        assertThat(DEV1.getEmail()).isEqualTo("DEV1@gmail.com");

    }
}
