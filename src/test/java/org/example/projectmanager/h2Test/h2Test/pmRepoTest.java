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

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:h2init.sql", executionPhase = BEFORE_TEST_METHOD)
public class pmRepoTest {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TeamRepository teamRepository;


    @Test
    void getUser() {

        userRepository.createUser(new User(0, "Marco", "123", "testX@gmail.com", userType.PROJECTMANAGER, devType.FULLSTACK, 7));
        userRepository.createUser(new User(1, "Goden", "adgangskode", "goden@gmail.com", userType.DEV, devType.FRONTEND, 5));
        userRepository.createUser(new User(2, "Rune", "321", "Misser@gmail.com", userType.ADMIN, devType.FULLSTACK, 8));


        List<User> users = userRepository.getUsers();

        assertThat(users).hasSize(4);

    }


    @Test
    void createAndFindUserByUsername() {

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

    @Test
    void creatProject(){

        Project projectX = new Project(1,
                "Projekt X",
                "Beskrivelse",
                "Ufærdrigt",
                "Høj",
                150,
                200,
                LocalDate.of(2025,12,10)
                , LocalDate.of(2026,1,1));


        int madeProject = projectRepository.createProject(projectX);

        assertThat(madeProject).isEqualTo(1);

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
        Project originalProject = new Project(0,
                "orginal navn",
                "Original beskrivelse",
                "ufærdigt",
                "højt",
                200,
                300,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2026,1,30));

        projectRepository.createProject(originalProject);

        Project editProject = new Project(0,
                "Redigeret navn",
                "Redigeret beskrivelse",
                "færdigt",
                "lav",
                200,
                400,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2026,2,10));

        projectRepository.editProject(editProject);

        Project resultat = projectRepository.findProject(0);

        assertEquals("Redigeret navn", resultat.getName());
        assertEquals("Redigeret Description", resultat.getDescription());
        assertEquals("færdigt", resultat.getStatus());
        assertEquals("lav", resultat.getPriority());
        assertEquals(200, resultat.getEstimatedTime());
        assertEquals(400, resultat.getActualTime());
        assertEquals(LocalDate.of(2026, 2, 10), resultat.getEndDate());


    }


    @Test
    void getTeam(){
        teamRepository.createTeam(new Team(1, "Team Alpha", "Det bedste team"));
        teamRepository.createTeam(new Team(2, "Team Beta", "Det næst bedste team"));
        teamRepository.createTeam(new Team(3, "Team Omega", "Det tredje bedste team"));

        List<Team>teams = teamRepository.getTeams();

        assertThat(teams).hasSize(3);


    }

    @Test
    void assignUsersToTeam(){
       userRepository.createUser(new User(1,
                "bob",
                "123",
                "bobTheGreatest@gmai.com",
                userType.PROJECTMANAGER,
                devType.FULLSTACK,
                6));
        userRepository.createUser(new User(2,
                "Frank",
                "007",
                "FrankBond@gmail.com",
                userType.DEV,
                devType.BACKEND,
                7 ));
        userRepository.createUser(new User(3,
                "Timothy",
                "Charlatan",
                "Tim@gmail.com",
                userType.DEV,
                devType.FRONTEND,
                8));



        Team team = new Team(0, "Test team", "test beskrivelse");
        int teamId = teamRepository.createTeam(team);

        teamRepository.assignUserToTeam(1,teamId);
        teamRepository.assignUserToTeam(2,teamId);
        teamRepository.assignUserToTeam(3,teamId);

        List<User> teamMembers = teamRepository.getUsersByTeamId(teamId);

        assertEquals(3, teamMembers.size());
        assertEquals(1, teamMembers.get(0).getUserId());

    }


    @Test
    void removeUserFromTeam(){
        userRepository.createUser(new User(1,
                "bob",
                "123",
                "bobTheGreatest@gmail.com",
                userType.PROJECTMANAGER,
                devType.FULLSTACK,
                6));

        userRepository.createUser(new User(2,
                "Frank",
                "007",
                "FrankBond@gmail.com",
                userType.DEV,
                devType.BACKEND,
                7));

        Team team = new Team(0, "Test team", "test beskrivelse");
        int teamId = teamRepository.createTeam(team);

        teamRepository.assignUserToTeam(1, teamId);
        teamRepository.assignUserToTeam(2, teamId);

        List<User> teamMembersBefore = teamRepository.getUsersByTeamId(teamId);


        assertEquals(2, teamMembersBefore.size());


        teamRepository.removeUserFromTeam(1, teamId);



        List<User> teamMembersAfter = teamRepository.getUsersByTeamId(teamId);

        assertEquals(1, teamMembersAfter.size());
        assertEquals(2, teamMembersAfter.get(0).getUserId());

    }




}