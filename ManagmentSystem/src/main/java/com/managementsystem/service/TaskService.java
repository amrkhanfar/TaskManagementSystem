package com.managementsystem.service;

import java.util.List;

import com.managementsystem.dao.EmployeeDAO;
import com.managementsystem.dao.EmployeeTaskDAO;
import com.managementsystem.dao.TaskDAO;
import com.managementsystem.exception.AuthorizationException;
import com.managementsystem.model.Employee;
import com.managementsystem.model.Role;
import com.managementsystem.model.Task;
import com.managementsystem.model.TaskStatus;
import com.managementsystem.model.Team;

public class TaskService implements TaskServiceInterface {
    TaskDAO taskDAO = new TaskDAO();
    EmployeeDAO employeeDAO = new EmployeeDAO();
    EmployeeTaskDAO employeeTaskDAO = new EmployeeTaskDAO();
    TeamService teamService = new TeamService();

    @Override
    public int createTask(Task task) {
	int generatedId = taskDAO.addTask(task);
	return generatedId;
    }

    @Override
    public boolean assignTask(int taskId, int employeeId) {
	Employee assignee = employeeDAO.getEmployeeById(employeeId);
	if (assignee == null) {
	    throw new IllegalArgumentException("Assignee not found.");
	}
	return employeeTaskDAO.assignTaskToEmployee(taskId, employeeId);
    }

    @Override
    public void approveTask(int taskId, int approverId) throws AuthorizationException {
	Task task = taskDAO.getTaskById(taskId);
	if (task == null) {
	    throw new IllegalArgumentException("Task not found.");
	}
	if (task.getTask_status() == TaskStatus.ASSIGNED_IN_PROGRESS) {
	    throw new IllegalArgumentException("Task already approved.");
	}

	Employee approver = employeeDAO.getEmployeeById(approverId);
	if (approver == null) {
	    throw new IllegalArgumentException("Approver not found.");
	}

	int approverRole = approver.getRole_id();
	if (approverRole == Role.MANAGER) {
	    
	    /* Manager can assign tasks */
	} else if (approverRole == Role.TEAM_LEADER) {

	    if (approver.getTeam_id() == approver.getTeam_id()) {
		
		/* Team leader can assign tasks to their team members*/
	    } else {
		throw new AuthorizationException("Team leaders can only assign tasks to their team memebers");
	    }
	} else {
	    
	    /* Employees can't assign tasks*/
	    throw new AuthorizationException("You don't have premission to assign tasks");
	}
	updateTaskStatus(taskId, TaskStatus.ASSIGNED_IN_PROGRESS, approverId);
    }

    public List<Task> getAllPendingTasks() {
	return taskDAO.getAllPendingTasks();
    }

    @Override
    public boolean updateTaskStatus(int taskId, int statusId, int updaterId) throws AuthorizationException {

	Task task = taskDAO.getTaskById(taskId);
	if (task == null) {
	    throw new IllegalArgumentException("Task not found.");
	}

	Employee updater = employeeDAO.getEmployeeById(updaterId);
	if (updater == null) {
	    throw new IllegalArgumentException("Updater not found.");
	}
	task.setTask_status(statusId);
	return taskDAO.updateTask(task);
    }

    @Override
    public List<Task> getPendingTasksForEmployee(int employeeId) {
	return taskDAO.getPendingTasksForEmployee(employeeId);
    }

    @Override
    public boolean updateTask(Task task) {
	return taskDAO.updateTask(task);
    }

    @Override
    public List<Task> getTasksByEmployee(int employeeId) {
	return employeeTaskDAO.getTasksByEmployeeId(employeeId);
    }

    @Override
    public Task getTaskById(int taskId) {
	return taskDAO.getTaskById(taskId);
    }

    @Override
    public List<Task> getAllTasks() {
	return taskDAO.getAllTasks();
    }
}
