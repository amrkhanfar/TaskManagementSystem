package com.managementsystem.model;

/**
 * 
* Task - Represents a task in the Task Management System.
*
* @author Amr
* @version Oct 7, 2024
 */
public class Task {
    private int id;
    private String task_title;
    private String task_description;
    private int task_status;
    
    /**
     * Constructs a new Team with the specified parameters.
     * 
     * @param id               The ID of the task.
     * @param task_title       The title of the task.
     * @param task_description The description of the task.
     * @param task_status      The status code of the task.
     */
    public Task(int id, String task_title, String task_description, int task_status) {
	super();
	this.id = id;
	this.task_title = task_title;
	this.task_description = task_description;
	this.task_status = task_status;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getTask_title() {
	return task_title;
    }

    public void setTask_title(String task_title) {
	this.task_title = task_title;
    }

    public String getTask_description() {
	return task_description;
    }

    public void setTask_description(String task_description) {
	this.task_description = task_description;
    }

    public int getTask_status() {
	return task_status;
    }

    public void setTask_status(int task_status) {
	this.task_status = task_status;
    }

    @Override
    public String toString() {
	return "Task [id=" + id + ", task_title=" + task_title + ", task_description=" + task_description
		+ ", task_status=" + task_status + "]";
    }

}
