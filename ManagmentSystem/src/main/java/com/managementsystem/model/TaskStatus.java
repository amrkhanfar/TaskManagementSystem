package com.managementsystem.model;

public class TaskStatus {
	
	private int id;
	private String status_name;
	
	
	public static int PENDING = 1;
	public static int ASSIGNED_IN_PROGRESS = 2;
	public static int COMPLETED = 3;
	
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
	
	
}
