package com.managementsystem.service;

import java.util.List;

import com.managementsystem.dao.EmployeeDAO;
import com.managementsystem.dao.TeamDAO;
import com.managementsystem.exception.AuthorizationException;
import com.managementsystem.model.Employee;
import com.managementsystem.model.Role;
import com.managementsystem.model.Team;

public class TeamService implements TeamServiceInterface {
    EmployeeDAO employeeDAO = new EmployeeDAO();
    TeamDAO teamDAO = new TeamDAO();

    @Override
    public int createTeam(Team team, int creatorId) throws AuthorizationException {
	Employee creator = employeeDAO.getEmployeeById(creatorId);
	if (creator == null) {
	    throw new IllegalArgumentException("Creator not found");
	}
	if (creator.getRole_id() != Role.MANAGER) {
	    throw new AuthorizationException("Only managers can create new teams");
	}

	Employee teamLeader = employeeDAO.getEmployeeById(team.getTeam_leader_id());
	if (teamLeader == null) {
	    throw new IllegalArgumentException("The specified team leader not found");
	}
	if (teamLeader.getRole_id() != Role.TEAM_LEADER) {
	    throw new IllegalArgumentException("The specified team leader is not a team leader");
	}

	return teamDAO.addTeam(team);
    }

    @Override
    public Team getTeamById(int teamId) {
	// TODO Auto-generated method stub
	return teamDAO.getTeamById(teamId);
    }

    @Override
    public List<Team> getAllTeams() {
	// TODO Auto-generated method stub
	return teamDAO.getAllTeams();
    }

    @Override
    public boolean updateTeam(Team team, int updaterId) throws AuthorizationException {
	Employee updater = employeeDAO.getEmployeeById(updaterId);
	if (updater == null) {
	    throw new IllegalArgumentException("Updater not found");
	}
	if (updater.getRole_id() != Role.MANAGER) {
	    throw new AuthorizationException("Only managers can update teams");
	}

	Team teamToUpdate = teamDAO.getTeamById(team.getId());
	if (teamToUpdate == null) {
	    throw new IllegalArgumentException("The team to update not found");
	}

	/*Check if the new team leader is a team leader.*/
	if (team.getTeam_leader_id() != teamToUpdate.getTeam_leader_id()) {
	    Employee newTeamLeader = employeeDAO.getEmployeeById(team.getTeam_leader_id());

	    if (newTeamLeader == null) {
		throw new IllegalArgumentException("The new team leader not found");
	    }
	    if (newTeamLeader.getRole_id() != Role.TEAM_LEADER) {
		throw new IllegalArgumentException("The specified team leader is not a team leader");
	    }
	}
	return teamDAO.updateTeam(team);
    }

    @Override
    public Team getTeamByTeamLeaderId(int id) {
	return teamDAO.getTeamByTeamLeaderId(id);
    }

    @Override
    public boolean deleteTeam(int teamId, int deleterId) throws AuthorizationException {
	Employee deleter = employeeDAO.getEmployeeById(deleterId);
	if (deleter == null) {
	    throw new IllegalArgumentException("Deleter not found");
	}
	if (deleter.getRole_id() != Role.MANAGER) {
	    throw new AuthorizationException("Only managers can delete teams");
	}

	Team teamToDelete = teamDAO.getTeamById(teamId);
	if (teamToDelete == null) {
	    throw new IllegalArgumentException("The team to delete not found");
	}
	return teamDAO.deleteTeamById(teamId);
    }
}
