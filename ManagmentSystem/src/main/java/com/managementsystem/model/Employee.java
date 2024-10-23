package com.managementsystem.model;

/**
 * 
 * Employee - Represents employee in management system
 *
 * @author Amr
 * @version Oct 6, 2024
 */
public class Employee {
    private int id;
    private String name;
    private String email;
    private int role_id;
    private int team_id;
    private String password;

    /**
     * Constructs a new employee with the specified parameters
     * 
     * @param id
     * @param name
     * @param email
     * @param role_id
     * @param team_id
     * @param password
     */
    public Employee(int id, String name, String email, int role_id, int team_id, String password) {
	this.id = id;
	this.name = name;
	this.email = email;
	this.role_id = role_id;
	this.team_id = team_id;
	this.password = password;
    }

    /**
     * Returns the employee id
     * 
     * @return the id
     */
    public int getId() {
	return id;
    }

    /**
     * Sets the employee id
     * 
     * @param id
     */
    public void setId(int id) {
	this.id = id;
    }

    /**
     * Returns the employee name
     * 
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * Sets the employee name
     * 
     * @param name
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * Returns the employee email
     * 
     * @return the email
     */
    public String getEmail() {
	return email;
    }

    /**
     * Sets the employee email
     * 
     * @param email
     */
    public void setEmail(String email) {
	this.email = email;
    }

    /**
     * Returns the employee role id which is mapped to role name in the Role class.
     * 
     * @return the role id
     */
    public int getRole_id() {
	return role_id;
    }

    /**
     * Sets the employee role id. Roles are mapped in Role.java as constants
     * 
     * @param role_id
     */
    public void setRole_id(int role_id) {
	this.role_id = role_id;
    }

    /**
     * Returns the employee team id
     * 
     * @return the employee team id
     */
    public int getTeam_id() {
	return team_id;
    }

    /**
     * Sets the employee team id
     * 
     * @param team_id
     */
    public void setTeam_id(int team_id) {
	this.team_id = team_id;
    }

    /**
     * Returns the employee password
     * 
     * @return the password
     */
    public String getPassword() {
	return password;
    }

    /**
     * Sets the employee id
     * 
     * @param password
     */
    public void setPassword(String password) {
	this.password = password;
    }

    @Override
    public String toString() {
	return "Employee [id=" + id + ", name=" + name + ", email=" + email + "]";
    }
    
    

}
