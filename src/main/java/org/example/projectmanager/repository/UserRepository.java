package org.example.projectmanager.repository;

import org.example.projectmanager.model.User;
import org.example.projectmanager.model.devType;
import org.example.projectmanager.model.userType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate){
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
                        rs.getInt("workTime")

                ));
    }

    public User createUser(User user){
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
}
