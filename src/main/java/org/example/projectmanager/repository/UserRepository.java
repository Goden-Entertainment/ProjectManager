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
    
    public List<User> getUsers() {
        String sql = "SELECT * FROM USER";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        userType.valueOf(rs.getString("userType")),
                        devType.valueOf(rs.getString("devType")),
                        rs.getInt("workTime")

                ));
    }

    public User createUser(User user){
        String sql = "INSERT INTO USER (username, password, email, userType, devType, workTime) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getUserType(),
                user.getDevType(),
                user.getWorkTime()
                );
        return user;
    }



}
