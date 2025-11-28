package org.example.projectmanager.repository;

import org.example.projectmanager.model.Project;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectRepository {
    JdbcTemplate jdbcTemplate;

    public ProjectRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public Project createProject(Project project){
        String sqlCreate = "INSERT INTO PROJECT (projectName, projectDescription, estimatedTime, actualTime) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sqlCreate,
                project.getName(),
                project.getDescription(),
                project.getEstimatedTime(),
                project.getActualTime()
        );
        return project;
    }

    public List<Project> getProjects(){
        String sql = "SELECT * FROM PROJECT";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
        new Project(
                rs.getInt("projectId"),
                rs.getString("projectName"),
                rs.getString("projectDescription"),
                rs.getInt("estimatedTime"),
                rs.getInt("actualTime")
        ));
    }

}
