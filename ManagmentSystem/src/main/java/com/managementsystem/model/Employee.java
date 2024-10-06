package com.managementsystem.model;

public class Employee {
	
	private int id;
	private String name;
	private String email;
	private int role_id;
	private int team_id;
	private String password;

	
	public Employee(int id, String name, String email, int role_id, int team_id, String password) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.role_id = role_id;
		this.team_id = team_id;
		this.password = password;
	}
	
	


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

	public int getTeam_id() {
		return team_id;
	}

	public void setTeam_id(int team_id) {
		this.team_id = team_id;
	}
	
	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", email=" + email + ", role_id=" + role_id + ", team_id="
				+ team_id + "]";
	}
	
	
}
