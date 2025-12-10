package org.example.projectmanager.repository;

import org.example.projectmanager.model.Subtask;
import org.example.projectmanager.model.User;
import org.example.projectmanager.model.devType;
import org.example.projectmanager.model.userType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubtaskRepository {

    private JdbcTemplate jdbcTemplate;

    public SubtaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // CREATE
    public int createSubtask(Subtask subtask) {
        String sqlInsert = "INSERT INTO SUBTASK (subTaskName, subTaskDescription, status, estimatedTime, actualTime, priority, startDate, endDate, task_id) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlInsert,
            subtask.getSubTaskName(),
            subtask.getSubTaskDescription(),
            subtask.getStatus(),
            subtask.getEstimatedTime(),
            subtask.getActualTime(),
            subtask.getPriority(),
            subtask.getStartDate(),
            subtask.getEndDate(),
            subtask.getTaskId()
        );

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        return jdbcTemplate.queryForObject(sqlGetId, Integer.class);
    }

    // READ ALL
    public List<Subtask> getSubtasks() {
        String sql = "SELECT * FROM SUBTASK";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
            new Subtask(
                rs.getInt("sub_task_id"),
                rs.getString("subTaskName"),
                rs.getString("subTaskDescription"),
                rs.getString("status"),
                rs.getInt("estimatedTime"),
                rs.getInt("actualTime"),
                rs.getString("priority"),
                rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null,
                rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null,
                rs.getInt("task_id")
            ));
    }

    // READ ONE
    public Subtask findSubtask(int subTaskId) {
        String sql = "SELECT * FROM SUBTASK WHERE sub_task_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{subTaskId}, (rs, rowNum) ->
            new Subtask(
                    rs.getInt("sub_task_id"),
                    rs.getString("subTaskName"),
                    rs.getString("subTaskDescription"),
                    rs.getString("status"),
                    rs.getInt("estimatedTime"),
                    rs.getInt("actualTime"),
                    rs.getString("priority"),
                    rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null,
                    rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null,
                    rs.getInt("task_id")
            ));
    }

    // UPDATE
    public void editSubtask(Subtask subtask) {
        String sql = "UPDATE SUBTASK SET subTaskName = ?, subTaskDescription = ?, status = ?, estimatedTime = ?, actualTime = ?, priority = ?, startDate = ?, endDate = ? " +
                     "WHERE sub_task_id = ?";
        jdbcTemplate.update(sql,
                subtask.getSubTaskName(),
                subtask.getSubTaskDescription(),
                subtask.getStatus(),
                subtask.getEstimatedTime(),
                subtask.getActualTime(),
                subtask.getPriority(),
                subtask.getStartDate(),
                subtask.getEndDate(),
                subtask.getSubTaskId()
        );
    }

    // DELETE
    public int deleteSubtask(int subTaskId) {
        String sql = "DELETE FROM SUBTASK WHERE sub_task_id = ?";
        return jdbcTemplate.update(sql, subTaskId);
    }

    // Get subtasks by task ID
    public List<Subtask> getSubtasksByTaskId(int taskId) {
        String sql = "SELECT * FROM SUBTASK WHERE task_id = ?";
        return jdbcTemplate.query(sql, new Object[]{taskId}, (rs, rowNum) ->
            new Subtask(
                    rs.getInt("sub_task_id"),
                    rs.getString("subTaskName"),
                    rs.getString("subTaskDescription"),
                    rs.getString("status"),
                    rs.getInt("estimatedTime"),
                    rs.getInt("actualTime"),
                    rs.getString("priority"),
                    rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null,
                    rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null,
                    rs.getInt("task_id")
            ));
    }

    // Get parent task ID
    public int getTaskIdBySubtaskId(int subTaskId) {
        String sql = "SELECT task_id FROM SUBTASK WHERE sub_task_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{subTaskId}, Integer.class);
    }

    // ========== JUNCTION TABLE METHODS ==========

    // Assign user to subtask
    public void assignUserToSubtask(int userId, int subTaskId) {
        String sql = "INSERT INTO USERSS_SUBTASK (user_id, sub_task_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, subTaskId);
    }

    // Remove user from subtask
    public void removeUserFromSubtask(int userId, int subTaskId) {
        String sql = "DELETE FROM USERSS_SUBTASK WHERE user_id = ? AND sub_task_id = ?";
        jdbcTemplate.update(sql, userId, subTaskId);
    }

    // Get all users assigned to a subtask
    public List<User> getUsersBySubtaskId(int subTaskId) {
        String sql = "SELECT * FROM USERSS u " +
                     "JOIN USERSS_SUBTASK us ON u.user_id = us.user_id " +
                     "WHERE us.sub_task_id = ?";
        return jdbcTemplate.query(sql, new Object[]{subTaskId}, (rs, rowNum) ->
            new User(
                rs.getInt("user_id"),
                rs.getString("userName"),
                rs.getString("userPassword"),
                rs.getString("userEmail"),
                rs.getString("userType") != null ?userType.valueOf(rs.getString("userType")) : null,
                rs.getString("devType") != null ? devType.valueOf(rs.getString("devType")) : null,
                rs.getInt("workTime"),
                rs.getInt("team_id")
            ));
    }

    // Remove all users from subtask (used before reassignment in edit)
    public void removeAllUsersFromSubtask(int subTaskId) {
        String sql = "DELETE FROM USERSS_SUBTASK WHERE sub_task_id = ?";
        jdbcTemplate.update(sql, subTaskId);
    }
}
