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
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new User(
                        rs.getInt("user_id"),
                        rs.getString("userName"),
                        rs.getString("userPassword"),
                        rs.getString("userEmail"),
                        rs.getString("userType") != null ? userType.valueOf(rs.getString("userType")) : null,
                        rs.getString("devType") != null ? devType.valueOf(rs.getString("devType")) : null,
                        rs.getInt("workTime")

                ));
    }

    public User createUser(User user) {
        String sql = "INSERT INTO USERS (userName, userPassword, userEmail, userType, devType, workTime) VALUES (?, ?, ?, ?, ?, ?)";
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
        String sqlEdit = "UPDATE USERS SET userName = ?, userEmail = ?, userPassword = ?, userType = ?, devType = ?, workTime = ? WHERE user_id = ?";

        jdbcTemplate.update(
                sqlEdit,
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getUserType() != null ? user.getUserType().name() : null,
                user.getDevType() != null ? user.getDevType().name() : null,
                user.getWorkTime(),
                user.getUserId()
        );
    }


    //Henter enkel bruger fra databasen, *Husk at skrive det samme rækkefølge som dette*
    public User findUser(int userId) {
        String sqlFindUser = "SELECT * FROM USERS WHERE user_id = ?";

        return jdbcTemplate.queryForObject(sqlFindUser, new Object[]{userId}, (rs, rowNum) ->
                new User(
                        rs.getInt("user_id"),
                        rs.getString("userName"),
                        rs.getString("userPassword"),
                        rs.getString("userEmail"),
                        rs.getString("userType") != null ? userType.valueOf(rs.getString("userType")) : null,
                        rs.getString("devType") != null ? devType.valueOf(rs.getString("devType")) : null,
                        rs.getInt("workTime")
                ));
    }


    public int deleteUser(int user_id) {
        String sql = "DELETE FROM USERS WHERE user_id = ?";

        return jdbcTemplate.update(sql, user_id);

    }


    public User findUser(String username) {

        String sql = "SELECT * FROM USERS WHERE username = ?";

        try{
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                    new User(
                            rs.getInt("user_id"),
                            rs.getString("userName"),
                            rs.getString("userPassword"),
                            rs.getString("userEmail"),
                            rs.getString("userType") != null ? userType.valueOf(rs.getString("userType")) : null,
                            rs.getString("devType") != null ? devType.valueOf(rs.getString("devType")) : null,
                            rs.getInt("workTime")

                    ),
                    username
            );

        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            // Ingen bruger fundet returner null
            return null;
        }
    }

    // Get all DEV users (for team assignment)
    public List<User> getDevUsers() {
        String sql = "SELECT * FROM USERS WHERE userType = 'DEV'";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new User(
                        rs.getInt("user_id"),
                        rs.getString("userName"),
                        rs.getString("userPassword"),
                        rs.getString("userEmail"),
                        userType.valueOf(rs.getString("userType")),
                        rs.getString("devType") != null ? devType.valueOf(rs.getString("devType")) : null,
                        rs.getInt("workTime")
                ));
    }
}
