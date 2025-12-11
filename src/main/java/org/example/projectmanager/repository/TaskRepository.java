package org.example.projectmanager.repository;

import jakarta.servlet.http.HttpSession;
import org.example.projectmanager.model.Task;
import org.example.projectmanager.model.Team;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class TaskRepository {
    JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createTask(Task task) {
        String sqlInsert = "INSERT INTO TASK (taskName, taskDescription, status, estimatedTime, actualTime, priority, startDate, endDate, sub_project_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlInsert,
                task.getName(),
                task.getDescription(),
                task.getStatus(),
                task.getEstimatedTime(),
                task.getActualTime(),
                task.getPriority(),
                task.getStartDate() != null ? Date.valueOf(task.getStartDate()) : null,
                task.getEndDate() != null ? Date.valueOf(task.getEndDate()) : null,
                task.getSubProjectId()
        );

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        return jdbcTemplate.queryForObject(sqlGetId, Integer.class);
    }

    public List<Task> getTasks() {
        String sql = "SELECT * FROM TASK";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Task(
                        rs.getInt("task_id"),
                        rs.getString("taskName"),
                        rs.getString("taskDescription"),
                        rs.getString("status"),
                        rs.getInt("estimatedTime"),
                        rs.getInt("actualTime"),
                        rs.getString("priority"),
                        rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null,
                        rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null,
                        rs.getInt("sub_project_id")
                ));
    }

    public Task findTask(int taskId) {
        String sqlFindTask = "SELECT * FROM TASK WHERE task_id = ?";
        return jdbcTemplate.queryForObject(sqlFindTask, new Object[]{taskId}, (rs, rowNum) ->
                new Task(
                        rs.getInt("task_id"),
                        rs.getString("taskName"),
                        rs.getString("taskDescription"),
                        rs.getString("status"),
                        rs.getInt("estimatedTime"),
                        rs.getInt("actualTime"),
                        rs.getString("priority"),
                        rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null,
                        rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null,
                        rs.getInt("sub_project_id")
                ));
    }

    public void editTask(Task task) {
        String sqlEdit = "UPDATE TASK SET taskName = ?, taskDescription = ?, status = ?, estimatedTime = ?, actualTime = ?, priority = ?, startDate = ?, endDate = ?, sub_project_id = ? WHERE task_id = ?";
        jdbcTemplate.update(
                sqlEdit,
                task.getName(),
                task.getDescription(),
                task.getStatus(),
                task.getEstimatedTime(),
                task.getActualTime(),
                task.getPriority(),
                task.getStartDate() != null ? Date.valueOf(task.getStartDate()) : null,
                task.getEndDate() != null ? Date.valueOf(task.getEndDate()) : null,
                task.getSubProjectId(),
                task.getTaskId()
        );
    }

    public int deleteTask(int taskId) {
        String sql = "DELETE FROM TASK WHERE task_id = ?";
        return jdbcTemplate.update(sql, taskId);
    }

    // Get tasks for a specific subproject (simplified - no junction table)
    public List<Task> getTasksBySubProjectId(int subProjectId) {
        String sql = "SELECT * FROM TASK WHERE sub_project_id = ?";
        return jdbcTemplate.query(sql, new Object[]{subProjectId}, (rs, rowNum) ->
                new Task(
                        rs.getInt("task_id"),
                        rs.getString("taskName"),
                        rs.getString("taskDescription"),
                        rs.getString("status"),
                        rs.getInt("estimatedTime"),
                        rs.getInt("actualTime"),
                        rs.getString("priority"),
                        rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null,
                        rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null,
                        rs.getInt("sub_project_id")
                ));
    }

    // Get subproject ID for a specific task (simplified - direct FK access)
    public int getSubProjectIdByTaskId(int taskId) {
        String sql = "SELECT sub_project_id FROM TASK WHERE task_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{taskId}, Integer.class);
    }
}
