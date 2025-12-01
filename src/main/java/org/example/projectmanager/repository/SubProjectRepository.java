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
        String sqlInsert = "INSERT INTO SUBPROJECT (subProjectName, subProjectDescription, team, status, estimatedTime, actualTime, startDate, endDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlInsert,
                subProject.getName(),
                subProject.getDescription(),
                subProject.getTeam(),
                subProject.getStatus(),
                subProject.getEstimatedTime(),
                subProject.getActualTime(),
                subProject.getStartDate() != null ? Date.valueOf(subProject.getStartDate()) : null,
                subProject.getEndDate() != null ? Date.valueOf(subProject.getEndDate()) : null
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
                        rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null
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
                        rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null
                ));
    }

    public void editSubProject(SubProject subProject) {
        String sqlEdit = "UPDATE SUBPROJECT SET subProjectName = ?, subProjectDescription = ?, team = ?, status = ?, estimatedTime = ?, actualTime = ?, startDate = ?, endDate = ? WHERE sub_project_id = ?";
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
                subProject.getSubProjectId()
        );
    }

    public int deleteSubProject(int subProjectId) {
        String sql = "DELETE FROM SUBPROJECT WHERE sub_project_id = ?";
        return jdbcTemplate.update(sql, subProjectId);
    }

    // Junction table method - assign subproject to project
    public void assignSubProjectToProject(int subProjectId, int projectId) {
        String sql = "INSERT INTO PROJECT_SUBPROJECT (project_id, sub_project_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, projectId, subProjectId);
    }

    // Junction table method - remove subproject from project
    public void removeSubProjectFromProject(int subProjectId, int projectId) {
        String sql = "DELETE FROM PROJECT_SUBPROJECT WHERE project_id = ? AND sub_project_id = ?";
        jdbcTemplate.update(sql, projectId, subProjectId);
    }

    // Get subprojects for a specific project (via junction table)
    public List<SubProject> getSubProjectsByProjectId(int projectId) {
        String sql = "SELECT sp.* FROM SUBPROJECT sp " +
                     "JOIN PROJECT_SUBPROJECT ps ON sp.sub_project_id = ps.sub_project_id " +
                     "WHERE ps.project_id = ?";
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
                        rs.getDate("endDate") != null ? rs.getDate("endDate").toLocalDate() : null
                ));
    }

    // Get projects for a specific subproject (many-to-many reverse lookup)
    public List<Integer> getProjectIdsBySubProjectId(int subProjectId) {
        String sql = "SELECT project_id FROM PROJECT_SUBPROJECT WHERE sub_project_id = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{subProjectId}, Integer.class);
    }
}
