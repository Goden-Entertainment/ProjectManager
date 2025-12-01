package org.example.projectmanager.repository;

import org.example.projectmanager.model.SubProject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class SubProjectRepository {
    JdbcTemplate jdbcTemplate;

    public SubProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createSubProject(SubProject subProject) {
        String sqlInsert = "INSERT INTO SUBPROJECT (subProjectName, subProjectDescription, team, status, estimatedTime, actualTime, startDate, endDate, project_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlInsert,
                subProject.getName(),
                subProject.getDescription(),
                subProject.getTeam(),
                subProject.getStatus(),
                subProject.getEstimatedTime(),
                subProject.getActualTime(),
                subProject.getStartDate() != null ? Date.valueOf(subProject.getStartDate()) : null,
                subProject.getEndDate() != null ? Date.valueOf(subProject.getEndDate()) : null,
                subProject.getProjectId()
        );

        String sqlGetId = "SELECT LAST_INSERT_ID()";
        return jdbcTemplate.queryForObject(sqlGetId, Integer.class);
    }

    public List<SubProject> getSubProjects() {
        String sql = "SELECT * FROM SUBPROJECT";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new SubProject(
                        rs.getInt("sub_project_id"),
                        rs.getString("subProjectName"),
                        rs.getString("subProjectDescription"),
                        rs.getString("team"),
                        rs.getString("status"),
                        rs.getInt("estimatedTime"),
                        rs.getInt("actualTime"),
                        rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null,
                        rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null,
                        rs.getInt("project_id")
                ));
    }

    public SubProject findSubProject(int subProjectId) {
        String sqlFindSubProject = "SELECT * FROM SUBPROJECT WHERE sub_project_id = ?";
        return jdbcTemplate.queryForObject(sqlFindSubProject, new Object[]{subProjectId}, (rs, rowNum) ->
                new SubProject(
                        rs.getInt("sub_project_id"),
                        rs.getString("subProjectName"),
                        rs.getString("subProjectDescription"),
                        rs.getString("team"),
                        rs.getString("status"),
                        rs.getInt("estimatedTime"),
                        rs.getInt("actualTime"),
                        rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null,
                        rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null,
                        rs.getInt("project_id")
                ));
    }

    public void editSubProject(SubProject subProject) {
        String sqlEdit = "UPDATE SUBPROJECT SET subProjectName = ?, subProjectDescription = ?, team = ?, status = ?, estimatedTime = ?, actualTime = ?, startDate = ?, endDate = ?, project_id = ? WHERE sub_project_id = ?";
        jdbcTemplate.update(
                sqlEdit,
                subProject.getName(),
                subProject.getDescription(),
                subProject.getTeam(),
                subProject.getStatus(),
                subProject.getEstimatedTime(),
                subProject.getActualTime(),
                subProject.getStartDate() != null ? Date.valueOf(subProject.getStartDate()) : null,
                subProject.getEndDate() != null ? Date.valueOf(subProject.getEndDate()) : null,
                subProject.getProjectId(),
                subProject.getSubProjectId()
        );
    }

    public int deleteSubProject(int subProjectId) {
        String sql = "DELETE FROM SUBPROJECT WHERE sub_project_id = ?";
        return jdbcTemplate.update(sql, subProjectId);
    }

    // Get subprojects for a specific project (simplified - no junction table)
    public List<SubProject> getSubProjectsByProjectId(int projectId) {
        String sql = "SELECT * FROM SUBPROJECT WHERE project_id = ?";
        return jdbcTemplate.query(sql, new Object[]{projectId}, (rs, rowNum) ->
                new SubProject(
                        rs.getInt("sub_project_id"),
                        rs.getString("subProjectName"),
                        rs.getString("subProjectDescription"),
                        rs.getString("team"),
                        rs.getString("status"),
                        rs.getInt("estimatedTime"),
                        rs.getInt("actualTime"),
                        rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null,
                        rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null,
                        rs.getInt("project_id")
                ));
    }

    // Get project ID for a specific subproject (simplified - direct FK access)
    public int getProjectIdBySubProjectId(int subProjectId) {
        String sql = "SELECT project_id FROM SUBPROJECT WHERE sub_project_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{subProjectId}, Integer.class);
    }
}
