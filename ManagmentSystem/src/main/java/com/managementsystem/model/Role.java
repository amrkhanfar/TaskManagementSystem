package com.managementsystem.model;

/**
 * Represents a role in the Task Management System.
 * 
 */
public class Role {
    
    
    /** Constant representing the Manager role ID in the database. */
    public static int MANAGER = 1;
    
    /** Constant representing the Team Leader role ID in the database. */
    public static int TEAM_LEADER = 2;
    
    /** Constant representing the Developer role ID in the database. */
    public static int DEVELOPER = 3;
    private int id;
    private String role_name;
    
    /**
     * Constructs a new Role with the specified parameters.
     * 
     * @param id         The ID of the role.
     * @param role_name  The name of the role.
     */
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
