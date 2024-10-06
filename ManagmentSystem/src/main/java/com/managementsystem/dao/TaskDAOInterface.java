package com.managementsystem.dao;

import java.util.List;

import com.managementsystem.model.Task;

public interface TaskDAOInterface {
	int addTask(Task task);
	Task getTaskById(int id);
	List<Task> getAllTasks();
	boolean updateTask(Task task);
	boolean deleteTaskById(int id);
	List<Task> getAllPendingTasks();
	List<Task> getPendingTasksForEmployee(int employeeId);
}