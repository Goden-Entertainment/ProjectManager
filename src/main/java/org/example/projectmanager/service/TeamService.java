package org.example.projectmanager.service;

import org.example.projectmanager.model.Team;
import org.example.projectmanager.model.User;
import org.example.projectmanager.repository.TeamRepository;
import org.example.projectmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    TeamRepository teamRepository;
    UserRepository userRepository;

    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    // Team CRUD methods
    public int createTeam(Team team) {
        return teamRepository.createTeam(team);
    }

    public List<Team> getTeams() {
        return teamRepository.getTeams();
    }

    public Team findTeam(int teamId) {
        return teamRepository.findTeam(teamId);
    }

    public void editTeam(Team team) {
        teamRepository.editTeam(team);
    }

    public int deleteTeam(int teamId) {
        return teamRepository.deleteTeam(teamId);
    }

    // Junction table methods
    public void assignUserToTeam(int userId, int teamId) {
        teamRepository.assignUserToTeam(userId, teamId);
    }

    public void removeUserFromTeam(int userId, int teamId) {
        teamRepository.removeUserFromTeam(userId, teamId);
    }

    public List<User> getUsersByTeamId(int teamId) {
        return teamRepository.getUsersByTeamId(teamId);
    }

    public List<Integer> getTeamIdsByUserId(int userId) {
        return teamRepository.getTeamIdsByUserId(userId);
    }

    // Get all DEV users (for team assignment)
    public List<User> getDevUsers() {
        return userRepository.getDevUsers();
    }
}
