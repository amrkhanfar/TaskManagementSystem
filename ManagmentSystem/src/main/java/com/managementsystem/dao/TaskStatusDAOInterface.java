package com.managementsystem.dao;

import java.util.List;

import com.managementsystem.model.TaskStatus;

public interface TaskStatusDAOInterface {
	
	List<TaskStatus> getAllStatus();
}
