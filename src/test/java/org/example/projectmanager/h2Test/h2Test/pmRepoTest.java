package org.example.projectmanager.h2Test.h2Test;

import org.example.projectmanager.model.*;

import org.example.projectmanager.repository.ProjectRepository;
import org.example.projectmanager.repository.TeamRepository;
import org.example.projectmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Sql(scripts = "classpath:h2init.sql", executionPhase = BEFORE_TEST_METHOD)
public class pmRepoTest {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void getUser() {

        userRepository.createUser(new User(0, "Marco", "123", "testX@gmail.com", userType.PROJECTMANAGER, devType.FULLSTACK, 7, null));
        userRepository.createUser(new User(1, "Goden", "adgangskode", "goden@gmail.com", userType.DEV, devType.FRONTEND, 5, null));
        userRepository.createUser(new User(2, "Rune", "321", "Misser@gmail.com", userType.ADMIN, devType.FULLSTACK, 8, null));


        List<User> users = userRepository.getUsers();

        assertThat(users).hasSize(3);

    }


    @Test
    void createAndFindUserByUsername() {

        User user = new User(2, "testuser", "secret", "test@email.com", userType.DEV, devType.BACKEND, 7, null);

        userRepository.createUser(user);
        User found = userRepository.findUser("testuser");

        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo("testuser");
        assertThat(found.getUserType()).isEqualTo(userType.DEV);
    }


    @Test
    void delete() {
        userRepository.createUser(
                new User(1, "Jens", "password", "email@.com", userType.DEV, devType.BACKEND, 7, null)
        );
        User existing = userRepository.findUser("Jens");

        userRepository.deleteUser(existing.getUserId());

        User afterDelete = userRepository.findUser("Jens");

        assertThat(afterDelete).isNull();
    }

    @Test
    void createProject(){
        Project projectX = new Project(null,
                "Projekt X",
                "Beskrivelse",
                "Ufærdrigt",
                "Høj",
                150,
                200,
                LocalDate.of(2025,12,10)
                , LocalDate.of(2026,1,1));

        //SAVE AND RETRIEVE PROJECTS
        int madeProjectXID = projectRepository.createProject(projectX);

        Project projectX2 = projectRepository.findProject(madeProjectXID);


        //CHECK THAT THE PROJECTS MATCH
        assertThat(projectX).usingRecursiveComparison().ignoringFields("projectId").isEqualTo(projectX2);
    }

    @Test
    void getProjects(){

        projectRepository.createProject(new Project(
                1,
                "navn",
                "beskrivelse",
                "status",
                "prioritet",
                100,
                200,
                LocalDate.of(2025, 12, 12),
                LocalDate.of(2026,1,1)
        ));
        projectRepository.createProject(new Project(
                2,
                "Projekt 2.0",
                "Bedre projekt",
                "ufærdigt",
                "højt",
                200,
                300,
                LocalDate.of(2025, 7, 12),
                LocalDate.of(2026,1,1)));

        List<Project> projects = projectRepository.getProjects();

        assertThat(projects).hasSize(2);

    }

    @Test
    void editProject(){
        Project originalProject = new Project(null,
                "orginal navn",
                "Original beskrivelse",
                "ufærdigt",
                "højt",
                200,
                300,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2026,1,30));

        int project1 = projectRepository.createProject(originalProject);

        Project editProject = projectRepository.findProject(project1);

        //CHECK THAT THE PROJECT HASNT BEEN ALTERED YET
        assertThat(originalProject).usingRecursiveComparison().ignoringFields("projectId").isEqualTo(editProject);

        //SET NEW PROJECT VALUES
        editProject.setName("Redigeret navn");
        editProject.setDescription("Redigeret beskrivelse");
        editProject.setStatus("færdigt");
        editProject.setPriority("lav");
        editProject.setEstimatedTime(200);
        editProject.setActualTime(400);
        editProject.setStartDate(LocalDate.of(2025, 1, 1));
        editProject.setEndDate(LocalDate.of(2026,2,10));


        //WRITE ALTERATIONS TO THE DATABASE
        projectRepository.editProject(editProject);

        //CHECK IF THE ALTERATIONS ARE CORRECT
        Project resultProject = projectRepository.findProject(project1);

        assertThat(editProject).usingRecursiveComparison().ignoringFields("projectId").isEqualTo(resultProject);
    }


    @Test
    void getTeam(){
        teamRepository.createTeam(new Team(1, "Team Alpha", "Det bedste team", null, null, null));
        teamRepository.createTeam(new Team(2, "Team Beta", "Det næst bedste team", null, null, null));
        teamRepository.createTeam(new Team(3, "Team Omega", "Det tredje bedste team", null, null, null));

        List<Team>teams = teamRepository.getTeams();

        assertThat(teams).hasSize(3);
    }

    @Test
    void assignUsersToTeam(){
        Team team = new Team(null, "Test team", "test beskrivelse", null, null, null);
        int teamId = teamRepository.createTeam(team);

        int user1 = userRepository.createUser(new User(null,
                "bob",
                "123",
                "bobTheGreatest@gmai.com",
                userType.DEV,
                devType.FULLSTACK,
                6,
                teamId));

        int user2 = userRepository.createUser(new User(null,
                "Frank",
                "007",
                "FrankBond@gmail.com",
                userType.DEV,
                devType.BACKEND,
                7,
                teamId));

        int user3 = userRepository.createUser(new User(null,
                "Timothy",
                "Charlatan",
                "Tim@gmail.com",
                userType.DEV,
                devType.FRONTEND,
                8,
                teamId));


        List<User> teamMembers = userRepository.getTeamDevs(teamId);

        //CHECK THAT ALL TEAM MEMBERS ARE PRESENT
        assertThat(teamMembers).hasSize(3);

        assertEquals(user1, teamMembers.get(0).getUserId());
        assertEquals(user2, teamMembers.get(1).getUserId());
        assertEquals(user3, teamMembers.get(2).getUserId());
    }


    @Test
    void removeUserFromTeam(){
        Team team = new Team(null, "Test team", "test beskrivelse", null, null, null);
        int teamId = teamRepository.createTeam(team);

        int user1 = userRepository.createUser(new User(null,
                "bub",
                "123",
                "bobTheGreatest@gmail.com",
                userType.DEV,
                devType.FULLSTACK,
                6,
                teamId));

        int user2 = userRepository.createUser(new User(null,
                "Frank",
                "007",
                "FrankBond@gmail.com",
                userType.DEV,
                devType.BACKEND,
                7,
                teamId));

        List<User> teamMembersBefore = userRepository.getTeamDevs(teamId);

        assertThat(teamMembersBefore).hasSize(2);

        //USER2 REMOVED FROM TEAM
        User dev = userRepository.findUser(user2);
        dev.setTeamId(null);

        userRepository.editUser(dev);

        List<User> teamMembersAfter = userRepository.getTeamDevs(teamId);

        //CHECKING TEAM SIZE
        assertThat(teamMembersAfter).hasSize(1);
        //CHECKING IF USER1 IS STILL ON THE TEAM
        assertEquals(user1, teamMembersAfter.get(0).getUserId());

    }




}