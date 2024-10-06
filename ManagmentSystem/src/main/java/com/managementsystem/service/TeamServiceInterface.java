package com.managementsystem.service;

import java.util.List;

import com.managementsystem.exception.AuthorizationException;
import com.managementsystem.model.Team;

public interface TeamServiceInterface {
	int createTeam(Team team, int creatorId) throws AuthorizationException;
	Team getTeamById(int teamId);
	List<Team> getAllTeams();
	boolean updateTeam(Team team, int updaterId) throws AuthorizationException;
	public Team getTeamByTeamLeaderId(int id);
	boolean deleteTeam(int teamId, int deleterId) throws AuthorizationException;
}
