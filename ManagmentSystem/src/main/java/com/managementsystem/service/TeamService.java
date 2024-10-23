package com.managementsystem.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.managementsystem.dao.EmployeeDAO;
import com.managementsystem.dao.TeamDAO;
import com.managementsystem.exception.AuthorizationException;
import com.managementsystem.model.Employee;
import com.managementsystem.model.Role;
import com.managementsystem.model.Team;

public class TeamService implements TeamServiceInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeamService.class);
    
    EmployeeDAO employeeDAO = new EmployeeDAO();
    TeamDAO teamDAO = new TeamDAO();

    @Override
    public int createTeam(Team team, int creatorId) throws AuthorizationException {
        LOGGER.debug("Entering createTeam with team: {} and creatorId: {}", team, creatorId);

        Employee creator = employeeDAO.getEmployeeById(creatorId);
        if (creator == null) {
            LOGGER.warn("Creating team failed: Creator not found with id: {}", creatorId);
            throw new IllegalArgumentException("Creator not found");
        }
        if (creator.getRole_id() != Role.MANAGER) {
            LOGGER.warn("Creating team failed: Only managers can create teams");
            throw new AuthorizationException("Only managers can create new teams");
        }

        Employee teamLeader = employeeDAO.getEmployeeById(team.getTeam_leader_id());
        if (teamLeader == null) {
            LOGGER.warn("Creating team failed: Team leader not found with id: {}", team.getTeam_leader_id());
            throw new IllegalArgumentException("The specified team leader not found");
        }
        if (teamLeader.getRole_id() != Role.TEAM_LEADER) {
            LOGGER.warn("Creating team failed: The specified team leader is not a team leader");
            throw new IllegalArgumentException("The specified team leader is not a team leader");
        }

        int teamId = teamDAO.addTeam(team);
        LOGGER.info("Team created successfully with generated id: {}", teamId);
        LOGGER.debug("Exiting createTeam with generated teamId: {}", teamId);
        return teamId;
    }

    @Override
    public Team getTeamById(int teamId) {
        LOGGER.debug("Entering getTeamById with teamId: {}", teamId);
        
        Team team = teamDAO.getTeamById(teamId);
        
        LOGGER.debug("Exiting getTeamById with team: {}", team);
        return team;
    }

    @Override
    public List<Team> getAllTeams() {
        LOGGER.debug("Entering getAllTeams");
        
        List<Team> teams = teamDAO.getAllTeams();
        
        LOGGER.debug("Exiting getAllTeams with {} teams", teams.size());
        return teams;
    }

    @Override
    public boolean updateTeam(Team team, int updaterId) throws AuthorizationException {
        LOGGER.debug("Entering updateTeam with team: {} and updaterId: {}", team, updaterId);
        
        Employee updater = employeeDAO.getEmployeeById(updaterId);
        if (updater == null) {
            LOGGER.warn("Updating team failed: Updater not found with id: {}", updaterId);
            throw new IllegalArgumentException("Updater not found");
        }
        if (updater.getRole_id() != Role.MANAGER) {
            LOGGER.warn("Updating team failed: Only managers can update teams");
            throw new AuthorizationException("Only managers can update teams");
        }

        Team teamToUpdate = teamDAO.getTeamById(team.getId());
        if (teamToUpdate == null) {
            LOGGER.warn("Updating team failed: Team not found with id: {}", team.getId());
            throw new IllegalArgumentException("The team to update not found");
        }

        if (team.getTeam_leader_id() != teamToUpdate.getTeam_leader_id()) {
            Employee newTeamLeader = employeeDAO.getEmployeeById(team.getTeam_leader_id());
            if (newTeamLeader == null) {
                LOGGER.warn("Updating team failed: New team leader not found with id: {}", team.getTeam_leader_id());
                throw new IllegalArgumentException("The new team leader not found");
            }
            if (newTeamLeader.getRole_id() != Role.TEAM_LEADER) {
                LOGGER.warn("Updating team failed: The specified new team leader is not a team leader");
                throw new IllegalArgumentException("The specified team leader is not a team leader");
            }
        }

        boolean isUpdated = teamDAO.updateTeam(team);
        LOGGER.info("Team with id: {} has been updated successfully", team.getId());
        LOGGER.debug("Exiting updateTeam with isUpdated: {}", isUpdated);
        return isUpdated;
    }

    @Override
    public Team getTeamByTeamLeaderId(int id) {
        LOGGER.debug("Entering getTeamByTeamLeaderId with teamLeaderId: {}", id);
        
        Team team = teamDAO.getTeamByTeamLeaderId(id);
        
        LOGGER.debug("Exiting getTeamByTeamLeaderId with team: {}", team);
        return team;
    }

    @Override
    public boolean deleteTeam(int teamId, int deleterId) throws AuthorizationException {
        LOGGER.debug("Entering deleteTeam with teamId: {} and deleterId: {}", teamId, deleterId);
        
        Employee deleter = employeeDAO.getEmployeeById(deleterId);
        if (deleter == null) {
            LOGGER.warn("Deleting team failed: Deleter not found with id: {}", deleterId);
            throw new IllegalArgumentException("Deleter not found");
        }
        if (deleter.getRole_id() != Role.MANAGER) {
            LOGGER.warn("Deleting team failed: Only managers can delete teams");
            throw new AuthorizationException("Only managers can delete teams");
        }

        Team teamToDelete = teamDAO.getTeamById(teamId);
        if (teamToDelete == null) {
            LOGGER.warn("Deleting team failed: Team not found with id: {}", teamId);
            throw new IllegalArgumentException("The team to delete not found");
        }

        boolean isDeleted = teamDAO.deleteTeamById(teamId);
        LOGGER.info("Team with id: {} has been deleted successfully", teamId);
        LOGGER.debug("Exiting deleteTeam with isDeleted: {}", isDeleted);
        return isDeleted;
    }
}
