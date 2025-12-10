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
                        rs.getInt("team_id"),
                        rs.getString("teamName"),
                        rs.getString("teamDescription"),
                        rs.getInt("project_id"),
                        rs.getInt("sub_project_id"),
                        rs.getInt("task_id")
                ));
    }

    // READ ONE
    public Team findTeam(int teamId) {
        String sql = "SELECT * FROM TEAM WHERE team_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{teamId}, (rs, rowNum) ->
                new Team(
                        rs.getInt("team_id"),
                        rs.getString("teamName"),
                        rs.getString("teamDescription"),
                        rs.getInt("project_id"),
                        rs.getInt("sub_project_id"),
                        rs.getInt("task_id")
                ));
    }

    // UPDATE
    public void editTeam(Team team) {
        String sql = "UPDATE TEAM SET teamName = ?, teamDescription = ? WHERE team_id = ?";
        jdbcTemplate.update(sql,
                team.getTeamName(),
                team.getTeamDescription(),
                team.getTeamId()
        );
    }

    // DELETE
    public int deleteTeam(int teamId) {
        String sql = "DELETE FROM TEAM WHERE team_id = ?";
        return jdbcTemplate.update(sql, teamId);
    }

    //Check naming convention for rettelser efter subtask er blevet lavet.
    public void assignTeamToSubTask(int teamId, int subTaskId) {
        String sql = "INSERT INTO TEAM_TASK (team_id, subTask_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, teamId, subTaskId);

    }

    public void assignTeamToSubProject(int teamId, int subProjectId) {
        String sql = "INSERT INTO SUBPROJECT_TEAM (team_id, task_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, teamId, subProjectId);
    }

    // Get teams for a specific user (reverse lookup)
    public List<Integer> getTeamIdsByUserId(int userId) {
        String sql = "SELECT team_id FROM USERS WHERE user_id = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{userId}, Integer.class);
    }
}
