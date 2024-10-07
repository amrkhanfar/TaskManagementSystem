package com.managementsystem.service;

import java.util.List;

import com.managementsystem.dao.TaskStatusDAO;
import com.managementsystem.model.TaskStatus;

public class TaskStatusService {
	private TaskStatusDAO taskStatusDAO = new TaskStatusDAO();
	
	public List<TaskStatus> getAllTaskStatus(){
		return taskStatusDAO.getAllStatuses();
	}
}
