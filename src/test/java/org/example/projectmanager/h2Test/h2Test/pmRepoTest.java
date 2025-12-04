package org.example.projectmanager.h2Test.h2Test;

import org.example.projectmanager.model.User;
import org.example.projectmanager.model.devType;
import org.example.projectmanager.model.userType;

import org.example.projectmanager.repository.ProjectRepository;
import org.example.projectmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:h2init.sql", executionPhase = BEFORE_TEST_METHOD)
public class pmRepoTest {


    @Autowired
    private UserRepository userRepository;


    @Test
    void getUser() {

        userRepository.createUser(new User(0, "Marco", "123", "testX@gmail.com", userType.PROJECTMANAGER, devType.FULLSTACK, 7));
        userRepository.createUser(new User(1, "Goden", "adgangskode", "goden@gmail.com", userType.DEV, devType.FRONTEND, 5));
        userRepository.createUser(new User(2, "Rune", "321", "Misser@gmail.com", userType.ADMIN, devType.FULLSTACK, 8));

        List<User> users = userRepository.getUsers();

        assertThat(users).hasSize(4);

    }


    @Test
    void createAndFindUser_byUsername() {

        User user = new User(2, "testuser", "secret", "test@email.com", userType.DEV, devType.BACKEND, 7
        );

        userRepository.createUser(user);
        User found = userRepository.findUser("testuser");

        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo("testuser");
        assertThat(found.getUserType()).isEqualTo(userType.DEV);
    }


    @Test
    void delete() {
        userRepository.createUser(
                new User(1, "Jens", "password", "email@.com", userType.DEV, devType.BACKEND, 7)
        );
        User existing = userRepository.findUser("Jens");

        userRepository.deleteUser(existing.getUserId());

        User afterDelete = userRepository.findUser("Jens");

        assertThat(afterDelete).isNull();
    }


}
