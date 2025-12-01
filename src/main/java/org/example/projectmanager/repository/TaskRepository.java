package org.example.projectmanager.repository;

import org.example.projectmanager.model.Task;
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
        String sqlInsert = "INSERT INTO TASK (taskName, taskDescription, assignedTeam, status, estimatedTime, actualTime, priority, startDate, endDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlInsert,
                task.getName(),
                task.getDescription(),
                task.getAssignedTeam(),
                task.getStatus(),
                task.getEstimatedTime(),
                task.getActualTime(),
                task.getPriority(),
                task.getStartDate() != null ? Date.valueOf(task.getStartDate()) : null,
                task.getEndDate() != null ? Date.valueOf(task.getEndDate()) : null
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
                        rs.getString("assignedTeam"),
                        rs.getString("status"),
                        rs.getInt("estimatedTime"),
                        rs.getInt("actualTime"),
                        rs.getString("priority"),
                        rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null,
                        rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null
                ));
    }

    public Task findTask(int taskId) {
        String sqlFindTask = "SELECT * FROM TASK WHERE task_id = ?";
        return jdbcTemplate.queryForObject(sqlFindTask, new Object[]{taskId}, (rs, rowNum) ->
                new Task(
                        rs.getInt("task_id"),
                        rs.getString("taskName"),
                        rs.getString("taskDescription"),
                        rs.getString("assignedTeam"),
                        rs.getString("status"),
                        rs.getInt("estimatedTime"),
                        rs.getInt("actualTime"),
                        rs.getString("priority"),
                        rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null,
                        rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null
                ));
    }

    public void editTask(Task task) {
        String sqlEdit = "UPDATE TASK SET taskName = ?, taskDescription = ?, assignedTeam = ?, status = ?, estimatedTime = ?, actualTime = ?, priority = ?, startDate = ?, endDate = ? WHERE task_id = ?";
        jdbcTemplate.update(
                sqlEdit,
                task.getName(),
                task.getDescription(),
                task.getAssignedTeam(),
                task.getStatus(),
                task.getEstimatedTime(),
                task.getActualTime(),
                task.getPriority(),
                task.getStartDate() != null ? Date.valueOf(task.getStartDate()) : null,
                task.getEndDate() != null ? Date.valueOf(task.getEndDate()) : null,
                task.getTaskId()
        );
    }

    public int deleteTask(int taskId) {
        String sql = "DELETE FROM TASK WHERE task_id = ?";
        return jdbcTemplate.update(sql, taskId);
    }

    // Junction table method - assign task to subproject
    public void assignTaskToSubProject(int taskId, int subProjectId) {
        String sql = "INSERT INTO SUBPROJECT_TASK (sub_project_id, task_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, subProjectId, taskId);
    }

    // Junction table method - remove task from subproject
    public void removeTaskFromSubProject(int taskId, int subProjectId) {
        String sql = "DELETE FROM SUBPROJECT_TASK WHERE sub_project_id = ? AND task_id = ?";
        jdbcTemplate.update(sql, subProjectId, taskId);
    }

    // Get tasks for a specific subproject (via junction table)
    public List<Task> getTasksBySubProjectId(int subProjectId) {
        String sql = "SELECT * FROM TASK t " +
                     "JOIN SUBPROJECT_TASK st ON t.task_id = st.task_id " +
                     "WHERE st.sub_project_id = ?";
        return jdbcTemplate.query(sql, new Object[]{subProjectId}, (rs, rowNum) ->
                new Task(
                        rs.getInt("task_id"),
                        rs.getString("taskName"),
                        rs.getString("taskDescription"),
                        rs.getString("assignedTeam"),
                        rs.getString("status"),
                        rs.getInt("estimatedTime"),
                        rs.getInt("actualTime"),
                        rs.getString("priority"),
                        rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null,
                        rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null
                ));
    }

    // Get subprojects for a specific task (many-to-many reverse lookup)
    public List<Integer> getSubProjectIdsByTaskId(int taskId) {
        String sql = "SELECT sub_project_id FROM SUBPROJECT_TASK WHERE task_id = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{taskId}, Integer.class);
    }
}
