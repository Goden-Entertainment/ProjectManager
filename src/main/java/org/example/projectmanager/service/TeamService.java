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

    public List<User> allAvailableDevs(){
        return userRepository.allAvailableDevs();
    }

    public List<User> getTeamDevs(int teamId){
        List<User> teamDevs = userRepository.getTeamDevs(teamId);

        return teamDevs;
    }

    public List<User> getAllDevs() {
        return userRepository.getAllDevs();
    }

    public void removeTeamMembers(int teamId){
        userRepository.removeTeamMembers(teamId);
    }
}
