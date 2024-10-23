package com.managementsystem.model;

/**
 * Represents the status of a task in the Task Management System.
 * 
 */
public class TaskStatus {
    
    /**
     * Constant representing the Pending status ID in the database.
     * You might have to change it based on the task_tatus table database.
     * 
     */
    public static int PENDING = 1;
    
    /**
     * Constant representing the Assigned status ID in the database.
     * In progress means that the assigned employee is working on it.
     * You might have to change it based on the task_tatus table database.
     * 
     */
    public static int ASSIGNED_IN_PROGRESS = 2;
    
    /**
     * Constant representing the Completed status ID in the database.
     * You might have to change it based on the task_tatus table database.
     * 
     */
    public static int COMPLETED = 3;
    
    private int id;
    private String status_name;
    
    /**
     * Constructs a new TaskStatus with the specified parameters.
     * 
     * @param id          The ID of the task status.
     * @param status_name The name of the task status.
     */
    public TaskStatus(int id, String status_name) {
	super();
	this.id = id;
	this.status_name = status_name;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getStatus_name() {
	return status_name;
    }

    public void setStatus_name(String status_name) {
	this.status_name = status_name;
    }

    @Override
    public String toString() {
	return "TaskStatus [id=" + id + ", status_name=" + status_name + "]";
    }
}
