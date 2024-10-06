package com.managementsystem.model;

public class Team {
	
	private int id;
	private String team_name;
	private int team_leader_id;
	
	
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
