package com.managementsystem.service;

import java.util.List;

import com.managementsystem.exception.AuthorizationException;
import com.managementsystem.model.Task;

public interface TaskServiceInterface {
	int createTask(Task task);
	boolean assignTask(int taskId, int employeeId);
	void approveTask(int taskId, int approverId) throws AuthorizationException;
	boolean updateTaskStatus(int taskId, int statusId, int updaterId) throws AuthorizationException;
	List<Task> getTasksByEmployee(int employeeId);
	Task getTaskById(int taskId);
	List<Task> getAllPendingTasks();
	List<Task> getPendingTasksForEmployee(int employeeId);
	List<Task> getAllTasks();
	boolean removeTaskFromEmployee(int taskId, int employeeId, int removerId) throws AuthorizationException;
	boolean deleteTask(int taskId, int deleterId) throws AuthorizationException;
	boolean updateTask(Task task); 
}
