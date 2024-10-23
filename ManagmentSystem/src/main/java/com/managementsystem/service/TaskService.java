package com.managementsystem.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
    
    TaskDAO taskDAO = new TaskDAO();
    EmployeeDAO employeeDAO = new EmployeeDAO();
    EmployeeTaskDAO employeeTaskDAO = new EmployeeTaskDAO();
    TeamService teamService = new TeamService();

    @Override
    public int createTask(Task task) {
	LOGGER.debug("Entering createTask with task = {}", task);
	
	int generatedId = taskDAO.addTask(task);
	
	LOGGER.debug("Exeting createTask with generatedId = {}", generatedId);
	return generatedId;
    }

    @Override
    public boolean assignTask(int taskId, int employeeId) {
	LOGGER.debug("Entering assignTask with taskId: {} and employeeId: {}", taskId, employeeId);
	
	Employee assignee = employeeDAO.getEmployeeById(employeeId);
	if (assignee == null) {
	    LOGGER.warn("Assigning task failed: no employee found with id: {}", employeeId);
	    throw new IllegalArgumentException("Assignee not found.");
	}
	
	Task task = taskDAO.getTaskById(taskId);
	if (task == null) {
	    LOGGER.warn("Assigning task failed: no task found with id: {}", taskId);
	    throw new IllegalArgumentException("Task not found.");
	}
	
	boolean isAssigned = employeeTaskDAO.assignTaskToEmployee(taskId, employeeId);
	
	LOGGER.info("Task: {} has been assigned to employee: {} successfuly", taskId, employeeId);
	LOGGER.debug("Exiting assignTask with isAssigned: {}", isAssigned);
	return isAssigned;
    }
    
    @Override
    public void changeToCompleted(int taskId, int updaterId) throws AuthorizationException {
	LOGGER.debug("Entering changeToCompleted with taskId: {} and udaterId: {}", taskId, updaterId);
	
	Task task = taskDAO.getTaskById(taskId);
	if (task == null) {
	    LOGGER.warn("Changing task status to completed failed: Task not found");
	    throw new IllegalArgumentException("Task not found.");
	}
	if (task.getTask_status() == TaskStatus.COMPLETED) {
	    LOGGER.warn("Changing task status to completed failed: task already is completed");
	    throw new IllegalArgumentException("Task is already completed.");
	}
	
	if (task.getTask_status() == TaskStatus.PENDING) {
	    LOGGER.warn("Changing task status to completed failed: Task is still awaiting for approval");
	    throw new IllegalArgumentException("Task is still awaiting for approval.");
	}

	Employee updater = employeeDAO.getEmployeeById(updaterId);
	if (updater == null) {
	    LOGGER.warn("Changing task status to completed failed: Updater not found");
	    throw new IllegalArgumentException("Updater not found.");
	}
	
	Employee assignedEmployee = employeeTaskDAO.getEmployeeByTaskId(taskId);
	if(assignedEmployee == null) {
	    LOGGER.warn("Changing task status to completed failed: No assigned employees to the task.");
	    throw new IllegalArgumentException("No assigned employees to the task.");
	}

	int approverRole = updater.getRole_id();
	if (approverRole == Role.MANAGER) {
	    
	    /* Manager can assign tasks */
	} else if (approverRole == Role.TEAM_LEADER) {

	    if (assignedEmployee.getTeam_id() == updater.getTeam_id()) {
		
		/* Team leader can modify tasks to their team members*/
	    } else {
		LOGGER.warn("Changing task status to completed failed: Team leaders can only modify tasks to their team memebers");
		throw new AuthorizationException("Team leaders can only modify tasks to their team memebers");
	    }
	} else if(updater.getId() == assignedEmployee.getId()) {
	    //Developor can mark their task as completed.
	    
	} else {
	    /* Employees can update their own task*/
	    LOGGER.warn("Changing task status to completed failed: Developers can modify their own tasks only");
	    throw new AuthorizationException("Developers can modify their own tasks only.");
	}
	boolean isUpdated = updateTaskStatus(taskId, TaskStatus.COMPLETED, updaterId);
	
	LOGGER.info("Task:{} status has been changed to completed successfuly", taskId);
	LOGGER.debug("Exiting changeToCompleted");
    }

    @Override
    public void approveTask(int taskId, int approverId) throws AuthorizationException {
	LOGGER.debug("Entering approveTask with taskId: {} and approverId: {}", taskId, approverId);
	
	Task task = taskDAO.getTaskById(taskId);
	if (task == null) {
	    LOGGER.warn("Approving task failed: Task not found");
	    throw new IllegalArgumentException("Task not found.");
	}
	if (task.getTask_status() == TaskStatus.ASSIGNED_IN_PROGRESS) {
	    LOGGER.warn("Approving task failed: Task already approved");
	    throw new IllegalArgumentException("Task already approved.");
	}

	Employee approver = employeeDAO.getEmployeeById(approverId);
	if (approver == null) {
	    LOGGER.warn("Approving task failed: Approver not found");
	    throw new IllegalArgumentException("Approver not found.");
	}
	
	Employee assignedEmployee = employeeTaskDAO.getEmployeeByTaskId(taskId);
	if(assignedEmployee == null) {
	    LOGGER.warn("Approving task failed: No assigned employees to the task");
	    throw new IllegalArgumentException("No assigned employees to the task.");
	}

	int approverRole = approver.getRole_id();
	if (approverRole == Role.MANAGER) {
	    
	    /* Manager can assign tasks */
	} else if (approverRole == Role.TEAM_LEADER) {

	    if (assignedEmployee.getTeam_id() == approver.getTeam_id()) {
		
		/* Team leader can assign tasks to their team members*/
	    } else {
		LOGGER.warn("Approving task failed: Team leaders can only approve tasks to their team memebers");
		throw new AuthorizationException("Team leaders can only approve tasks to their team memebers");
	    }
	} else {
	    
	    /* Employees can't assign tasks*/
	    LOGGER.warn("Approving task failed: Developers can modify their own tasks only");
	    throw new AuthorizationException("Developers can modify their own tasks only.");
	}
	
	updateTaskStatus(taskId, TaskStatus.ASSIGNED_IN_PROGRESS, approverId);
	LOGGER.info("Task:{} has been approved successfuly", taskId);
	LOGGER.debug("Exiting approveTask");
    }

    public List<Task> getAllPendingTasks() {
	LOGGER.debug("Entering getAllPendingTasks");
	
	List<Task> allPendingTasks = taskDAO.getAllPendingTasks();
	
	LOGGER.debug("Exiting getAllPendingTasks with {} pending tasks", allPendingTasks.size());
	return allPendingTasks;
    }

    @Override
    public boolean updateTaskStatus(int taskId, int statusId, int updaterId) throws AuthorizationException {
	LOGGER.debug("Entering updateTaskStatus with taskId: {}, statusId and updaterId: {}", taskId, statusId, updaterId);
	Task task = taskDAO.getTaskById(taskId);
	if (task == null) {
	    LOGGER.warn("Updating task status failed: Task not found");
	    throw new IllegalArgumentException("Task not found.");
	}

	Employee updater = employeeDAO.getEmployeeById(updaterId);
	if (updater == null) {
	    LOGGER.warn("Updating task status failed: Updater not found");
	    throw new IllegalArgumentException("Updater not found.");
	}
	task.setTask_status(statusId);
	LOGGER.info("Task: {} status has been changed to statusId: {} successfuly by updatedId: {}", taskId, statusId, updaterId);
	return taskDAO.updateTask(task);
    }

    @Override
    public List<Task> getPendingTasksForEmployee(int employeeId) {
        LOGGER.debug("Entering getPendingTasksForEmployee with employeeId: {}", employeeId);
        
        List<Task> pendingTasks = taskDAO.getPendingTasksForEmployee(employeeId);
        
        LOGGER.debug("Exiting getPendingTasksForEmployee with {} pending tasks", pendingTasks.size());
        return pendingTasks;
    }

    @Override
    public boolean updateTask(Task task) {
        LOGGER.debug("Entering updateTask with task: {}", task);
        
        boolean isUpdated = taskDAO.updateTask(task);
        
        LOGGER.debug("Exiting updateTask with isUpdated: {}", isUpdated);
        return isUpdated;
    }

    @Override
    public List<Task> getTasksByEmployee(int employeeId) {
        LOGGER.debug("Entering getTasksByEmployee with employeeId: {}", employeeId);
        
        List<Task> tasks = employeeTaskDAO.getTasksByEmployeeId(employeeId);
        
        LOGGER.debug("Exiting getTasksByEmployee with {} tasks", tasks.size());
        return tasks;
    }

    @Override
    public Task getTaskById(int taskId) {
        LOGGER.debug("Entering getTaskById with taskId: {}", taskId);
        
        Task task = taskDAO.getTaskById(taskId);
        
        LOGGER.debug("Exiting getTaskById with task: {}", task);
        return task;
    }

    @Override
    public List<Task> getAllTasks() {
        LOGGER.debug("Entering getAllTasks");
        
        List<Task> allTasks = taskDAO.getAllTasks();
        
        LOGGER.debug("Exiting getAllTasks with {} tasks", allTasks.size());
        return allTasks;
    }

    @Override
    public void deleteTask(int taskId) {
        LOGGER.debug("Entering deleteTask with taskId: {}", taskId);
        
        Task task = taskDAO.getTaskById(taskId);
        if (task == null) {
            LOGGER.warn("Deleting task failed: Task not found with id: {}", taskId);
            throw new IllegalArgumentException("Task not found.");
        }
        
        taskDAO.deleteTaskById(taskId);
        Employee assignedEmployee = employeeTaskDAO.getEmployeeByTaskId(taskId);
        if (assignedEmployee != null) {
            employeeTaskDAO.removeTaskFromEmployee(taskId, assignedEmployee.getId());
        }
        
        LOGGER.info("Task: {} has been deleted successfully", taskId);
        LOGGER.debug("Exiting deleteTask");
    }
}
