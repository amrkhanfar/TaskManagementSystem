package com.managementsystem.dao;


import java.util.List;

import com.managementsystem.model.Team;

public interface TeamDAOInterface {
	
	int addTeam(Team team);
	Team getTeamById(int id);
	Team getTeamByTeamLeaderId(int id);
	List<Team> getAllTeams();
	boolean updateTeam(Team team);
	boolean deleteTeamById(int id);
}
