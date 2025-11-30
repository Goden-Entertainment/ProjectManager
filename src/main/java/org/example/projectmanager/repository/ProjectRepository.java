package org.example.projectmanager.repository;

import org.example.projectmanager.model.SubProject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectRepository {
    JdbcTemplate jdbcTemplate;

    public ProjectRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public SubProject createSubProject(SubProject subProject){

        String sql = "INSERT INTO subProject (name, description, estimatedTime, actualTime) VALUES (?,?,?,?)";

        jdbcTemplate.update(sql,
                subProject.getName(),
                subProject.getDescription(),
                subProject.getEstimatedTime(),
                subProject.getActualTime());

        return subProject;
    }

    public void editSubProject(SubProject subProject){
        String sql = "UPDATE subProject SET name = ?, description = ?, estimatedTime = ?, actualTime = ? WHERE subProjectId = ?";

        jdbcTemplate.update( sql,
                subProject.getName(),
                subProject.getDescription(),
                subProject.getEstimatedTime(),
                subProject.getActualTime(),
                subProject.getSubProjectID());
    }

    public SubProject findSubProject( int subProjectId){
        String sql = "SELECT * FROM subProject WHERE subProjectId = ?";
       return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new SubProject(
                        rs.getInt("subProjectId"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("estimatedTime"),
                        rs.getInt("actualTime"))
                );

    }

    public int deleteSubProject(int subProjectId){
        String sqlDelete = "DELETE FROM subProject WHERE subProjectId = ?";
        return jdbcTemplate.update(sqlDelete, subProjectId);
    }

}
