package org.example.projectmanager.repository;

import org.example.projectmanager.model.Team;
import org.example.projectmanager.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeamRepository {
    JdbcTemplate jdbcTemplate;

    public TeamRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // CREATE - Returns generated ID
    public int createTeam(Team team) {
        String sqlInsert = "INSERT INTO TEAM (teamName, teamDescription) VALUES (?, ?)";
        jdbcTemplate.update(sqlInsert,
                team.getTeamName(),
                team.getTeamDescription()
        );

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        return jdbcTemplate.queryForObject(sqlGetId, Integer.class);
    }

    // READ ALL
    public List<Team> getTeams() {
        String sql = "SELECT * FROM TEAM";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Team(
                        rs.getObject("team_id", Integer.class),
                        rs.getString("teamName"),
                        rs.getString("teamDescription"),
                        rs.getObject("project_id", Integer.class),
                        rs.getObject("sub_project_id", Integer.class),
                        rs.getObject("task_id", Integer.class)
                ));
    }

    // READ ONE
    public Team findTeam(int teamId) {
        String sql = "SELECT * FROM TEAM WHERE team_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{teamId}, (rs, rowNum) ->
                new Team(
                        rs.getObject("team_id", Integer.class),
                        rs.getString("teamName"),
                        rs.getString("teamDescription"),
                        rs.getObject("project_id", Integer.class),
                        rs.getObject("sub_project_id", Integer.class),
                        rs.getObject("task_id", Integer.class)
                ));
    }

    // UPDATE
    public void editTeam(Team team) {
        String sql = "UPDATE TEAM SET teamName = ?, teamDescription = ?, project_id = ?, sub_project_id = ?, task_id = ? WHERE team_id = ?";
        jdbcTemplate.update(sql,
                team.getTeamName(),
                team.getTeamDescription(),
                team.getProjectId(),
                team.getSubProjectId(),
                team.getTaskId(),
                team.getTeamId()
        );
    }

    // DELETE
    public int deleteTeam(int teamId) {
        String sql = "DELETE FROM TEAM WHERE team_id = ?";
        return jdbcTemplate.update(sql, teamId);
    }


    public void assignTeamToProject(int teamId, int projectId) {
        String sql = "UPDATE TEAM SET project_id = ? WHERE team_id = ?";
        jdbcTemplate.update(sql, projectId, teamId);

    }

    public void assignTeamToSubProject(int teamId, int subProjectId) {
        String sql = "UPDATE TEAM SET sub_project_id = ? WHERE team_id = ?";
        jdbcTemplate.update(sql, subProjectId, teamId);
    }

    public void assignTeamToTask(int teamId, int taskId) {
        String sql = "UPDATE TEAM SET task_id = ? WHERE team_id = ?";
        jdbcTemplate.update(sql, taskId, teamId);
    }

    // Get teams for a specific user (reverse lookup)
    public List<Integer> getTeamIdsByUserId(int userId) {
        String sql = "SELECT team_id FROM USERS WHERE user_id = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{userId}, Integer.class);
    }

    public void removeProjectTeams(int projectId){
        String sql = "UPDATE TEAM SET project_id = NULL, sub_project_id = NULL, task_id = NULL WHERE project_id = ?";
        jdbcTemplate.update(sql, projectId);
    }

    public List<Team> allAvailableTeamsFor_Project() {
        String sql = "SELECT * FROM TEAM WHERE project_id IS NULL";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Team(
                        rs.getObject("team_id", Integer.class),
                        rs.getString("teamName"),
                        rs.getString("teamDescription"),
                        rs.getObject("project_id", Integer.class),
                        rs.getObject("sub_project_id", Integer.class),
                        rs.getObject("task_id", Integer.class)
                ));
    }

    public List<Team> allAvailableTeamsFor_Project(int projectId) {
        String sql = "SELECT * FROM TEAM WHERE project_id IS NULL OR project_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Team(
                        rs.getObject("team_id", Integer.class),
                        rs.getString("teamName"),
                        rs.getString("teamDescription"),
                        rs.getObject("project_id", Integer.class),
                        rs.getObject("sub_project_id", Integer.class),
                        rs.getObject("task_id", Integer.class)
                )
                , projectId);
    }

    public List<Team> allAvailableTeamsFor_SubProject() {
        String sql = "SELECT * FROM TEAM WHERE sub_project_id IS NULL";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Team(
                        rs.getObject("team_id", Integer.class),
                        rs.getString("teamName"),
                        rs.getString("teamDescription"),
                        rs.getObject("project_id", Integer.class),
                        rs.getObject("sub_project_id", Integer.class),
                        rs.getObject("task_id", Integer.class)
                ));
    }

    public List<Team> allAvailableTeamsFor_Task() {
        String sql = "SELECT * FROM TEAM WHERE task_id IS NULL";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Team(
                        rs.getObject("team_id", Integer.class),
                        rs.getString("teamName"),
                        rs.getString("teamDescription"),
                        rs.getObject("project_id", Integer.class),
                        rs.getObject("sub_project_id", Integer.class),
                        rs.getObject("task_id", Integer.class)
                ));
    }
}
