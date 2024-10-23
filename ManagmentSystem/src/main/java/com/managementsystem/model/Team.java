package com.managementsystem.model;

/**
 * 
 * Team - Represents a team in the Task Management System.
 *
 * @author Amr
 * @version Oct 7, 2024
 */
public class Team {
    private int id;
    private String team_name;
    private int team_leader_id;

    /**
     * Constructs a new Team with the specified parameters
     * 
     * @param id             The ID of the team.
     * @param team_name      The name of the team.
     * @param team_leader_id The ID of the team leader.
     */
    public Team(int id, String team_name, int team_leader_id) {
	this.id = id;
	this.team_name = team_name;
	this.team_leader_id = team_leader_id;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getTeam_name() {
	return team_name;
    }

    public void setTeam_name(String team_name) {
	this.team_name = team_name;
    }

    public int getTeam_leader_id() {
	return team_leader_id;
    }

    public void setTeam_leader_id(int team_leader_id) {
	this.team_leader_id = team_leader_id;
    }

}
