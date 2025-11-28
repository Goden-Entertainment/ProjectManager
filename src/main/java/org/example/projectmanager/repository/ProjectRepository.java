package org.example.projectmanager.repository;

import org.example.projectmanager.model.Project;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ProjectRepository {
    JdbcTemplate jdbcTemplate;

    public ProjectRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createProject(Project project){
        String sqlCreate = "INSERT INTO PROJECT (projectName, projectDescription, status, priority, estimatedTime, actualTime, startDate, endDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, project.getName());
            ps.setString(2, project.getDescription());
            ps.setString(3, project.getStatus());
            ps.setString(4, project.getPriority());
            ps.setInt(5, project.getEstimatedTime());
            ps.setInt(6, project.getActualTime());
            ps.setDate(7, project.getStartDate() != null ? Date.valueOf(project.getStartDate()) : null);
            ps.setDate(8, project.getEndDate() != null ? Date.valueOf(project.getEndDate()) : null);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public List<Project> getProjects(){
        String sql = "SELECT * FROM PROJECT";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
        new Project(
                rs.getInt("project_id"),
                rs.getString("projectName"),
                rs.getString("projectDescription"),
                rs.getString("status"),
                rs.getString("priority"),
                rs.getInt("estimatedTime"),
                rs.getInt("actualTime"),
                rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null,
                rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null
        ));
    }

    public Project findProject(int projectId){
        String sqlFindProject = "SELECT * FROM PROJECT WHERE project_id = ?";
        return jdbcTemplate.queryForObject(sqlFindProject, new Object[]{projectId}, (rs, rowNum) ->
                new Project(
                        rs.getInt("project_id"),
                        rs.getString("projectName"),
                        rs.getString("projectDescription"),
                        rs.getString("status"),
                        rs.getString("priority"),
                        rs.getInt("estimatedTime"),
                        rs.getInt("actualTime"),
                        rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null,
                        rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null
                ));
    }

    public void editProject(Project project){
        String sqlEdit = "UPDATE PROJECT SET projectName = ?, projectDescription = ?, status = ?, priority = ?, estimatedTime = ?, actualTime = ?, startDate = ?, endDate = ? WHERE project_id = ?";
        jdbcTemplate.update(
                sqlEdit,
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                project.getPriority(),
                project.getEstimatedTime(),
                project.getActualTime(),
                project.getStartDate() != null ? Date.valueOf(project.getStartDate()) : null,
                project.getEndDate() != null ? Date.valueOf(project.getEndDate()) : null,
                project.getProjectId()
        );
    }

    public int deleteProject(int projectId){
        String sql = "DELETE FROM PROJECT WHERE project_id = ?";
        return jdbcTemplate.update(sql, projectId);
    }

    public List<Project> getProjectsByUserId(int userId){
        String sql = "SELECT p.* FROM PROJECT p " +
                     "JOIN USER_PROJECT up ON p.project_id = up.project_id " +
                     "WHERE up.user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) ->
                new Project(
                        rs.getInt("project_id"),
                        rs.getString("projectName"),
                        rs.getString("projectDescription"),
                        rs.getString("status"),
                        rs.getString("priority"),
                        rs.getInt("estimatedTime"),
                        rs.getInt("actualTime"),
                        rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null,
                        rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null
                ));
    }

    public void assignProjectToUser(int projectId, int userId){
        String sql = "INSERT INTO USER_PROJECT (user_id, project_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, projectId);
    }

}
