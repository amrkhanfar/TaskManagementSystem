package com.managementsystem.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.managementsystem.dao.TaskStatusDAO;
import com.managementsystem.model.TaskStatus;

public class TaskStatusService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskStatusService.class);
    private TaskStatusDAO taskStatusDAO = new TaskStatusDAO();

    public List<TaskStatus> getAllTaskStatus() {
	LOGGER.debug("Entering getAllTaskStatus");
	
	List<TaskStatus> allStatuses = taskStatusDAO.getAllStatuses();
	
	LOGGER.debug("Exiting getAllTaskStatus with {} task statuses", allStatuses.size());
	return allStatuses;
    }
}
