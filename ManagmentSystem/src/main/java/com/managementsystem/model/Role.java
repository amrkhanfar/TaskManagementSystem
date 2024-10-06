package com.managementsystem.model;

public class Role {
	
	private int id;
	private String role_name;
	
	public static int MANAGER = 1;
	public static int TEAM_LEADER = 2;
	public static int DEVELOPER = 3;
	
	
	public Role(int id, String role_name) {
		super();
		this.id = id;
		this.role_name = role_name;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getRole_name() {
		return role_name;
	}


	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	
	
}
