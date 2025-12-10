package org.example.projectmanager.repository;

import org.example.projectmanager.model.User;
import org.example.projectmanager.model.devType;
import org.example.projectmanager.model.userType;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
    JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getUsers() {
        String sql = "SELECT * FROM USER";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new User(
                        rs.getInt("user_id"),
                        rs.getString("userName"),
                        rs.getString("userPassword"),
                        rs.getString("userEmail"),
                        rs.getString("userType") != null ? userType.valueOf(rs.getString("userType")) : null,
                        rs.getString("devType") != null ? devType.valueOf(rs.getString("devType")) : null,
                        rs.getInt("workTime"),
                        rs.getInt("team_id")
                ));
    }

    public User createUser(User user) {
        String sql = "INSERT INTO USER (userName, userPassword, userEmail, userType, devType, workTime) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getUserType() != null ? user.getUserType().name() : null,
                user.getDevType() != null ? user.getDevType().name() : null,
                user.getWorkTime()
        );
        return user;
    }

    public void editUser(User user) {
        String sqlEdit = "UPDATE USER SET userName = ?, userEmail = ?, userPassword = ?, userType = ?, devType = ?, workTime = ?, team_id = ? WHERE user_id = ?";

        jdbcTemplate.update(
                sqlEdit,
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getUserType() != null ? user.getUserType().name() : null,
                user.getDevType() != null ? user.getDevType().name() : null,
                user.getWorkTime(),
                user.getTeamId(),
                user.getUserId()
        );
    }


    //Henter enkel bruger fra databasen, *Husk at skrive det samme rækkefølge som dette*
    public User findUser(int userId) {
        String sqlFindUser = "SELECT * FROM USER WHERE user_id = ?";

        return jdbcTemplate.queryForObject(sqlFindUser, new Object[]{userId}, (rs, rowNum) ->
                new User(
                        rs.getInt("user_id"),
                        rs.getString("userName"),
                        rs.getString("userPassword"),
                        rs.getString("userEmail"),
                        rs.getString("userType") != null ? userType.valueOf(rs.getString("userType")) : null,
                        rs.getString("devType") != null ? devType.valueOf(rs.getString("devType")) : null,
                        rs.getInt("workTime"),
                        rs.getInt("team_id")
                ));
    }


    public int deleteUser(int user_id) {
        String sql = "DELETE FROM user where user_id = ?";

        return jdbcTemplate.update(sql, user_id);

    }


    public User findUser(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";

        try{
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                    new User(
                            rs.getInt("user_id"),
                            rs.getString("userName"),
                            rs.getString("userPassword"),
                            rs.getString("userEmail"),
                            rs.getString("userType") != null ? userType.valueOf(rs.getString("userType")) : null,
                            rs.getString("devType") != null ? devType.valueOf(rs.getString("devType")) : null,
                            rs.getInt("workTime"),
                            rs.getInt("team_id")
                    ),
                    username
            );

        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            // Ingen bruger fundet returner null
            return null;
        }
    }

    // Get all DEV users (for team assignment)
    public List<User> allAvailableDevs() {
        String sql = "SELECT * FROM USER WHERE userType = 'DEV' AND team_id IS NULL";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new User(
                        rs.getInt("user_id"),
                        rs.getString("userName"),
                        rs.getString("userPassword"),
                        rs.getString("userEmail"),
                        rs.getString("userType") != null ? userType.valueOf(rs.getString("userType")) : null,
                        rs.getString("devType") != null ? devType.valueOf(rs.getString("devType")) : null,
                        rs.getInt("workTime"),
                        rs.getInt("team_id")
                ));
    }

    public List<User> getAllDevs() {
        String sql = "SELECT * FROM USER WHERE userType = 'DEV'";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new User(
                        rs.getInt("user_id"),
                        rs.getString("userName"),
                        rs.getString("userPassword"),
                        rs.getString("userEmail"),
                        rs.getString("userType") != null ? userType.valueOf(rs.getString("userType")) : null,
                        rs.getString("devType") != null ? devType.valueOf(rs.getString("devType")) : null,
                        rs.getInt("workTime"),
                        rs.getInt("team_id")
                ));
    }

    public List<User> getTeamDevs(int teamId) {
        String sql = "SELECT * FROM USER WHERE userType = 'DEV' AND team_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new User(
                        rs.getInt("user_id"),
                        rs.getString("userName"),
                        rs.getString("userPassword"),
                        rs.getString("userEmail"),
                        rs.getString("userType") != null ? userType.valueOf(rs.getString("userType")) : null,
                        rs.getString("devType") != null ? devType.valueOf(rs.getString("devType")) : null,
                        rs.getInt("workTime"),
                        rs.getInt("team_id")
                )
                , teamId);
    }

    public void removeTeamMembers(int teamId){
        String sql = "UPDATE USER SET team_id = NULL WHERE team_id = ?";
        jdbcTemplate.update(sql, teamId);
    }
}
