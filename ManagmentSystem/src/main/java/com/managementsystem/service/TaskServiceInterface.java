package com.managementsystem.service;

import java.util.List;
import com.managementsystem.exception.AuthorizationException;
import com.managementsystem.model.Task;

/**
 * 
* TaskServiceInterface - Interface defining the service methods for Task operations.
*
* @author Amr
* @version Oct 8, 2024
 */
public interface TaskServiceInterface {

    /**
     * Creates a new task.
     * Note that the ID attribute is discarded since
     * the database generates an ID.
     *
     * @param task the Task object to create
     * @return the generated ID of the new task
     */
    int createTask(Task task);

    /**
     * Assigns a task to an employee.
     *
     * @param taskId     the ID of the task to assign
     * @param employeeId the ID of the employee to assign the task to
     * @return true if the assignment was successful, false otherwise
     */
    boolean assignTask(int taskId, int employeeId);

    /**
     * Approves a task.
     * Changes it's status from pending to assigned.
     * It's used when a developer creates a task for themself
     * And their team leader or a manager approves it.
     *
     * @param taskId     the ID of the task to approve
     * @param approverId the ID of the user approving the task
     * @throws AuthorizationException if the approver is not authorized
     */
    void approveTask(int taskId, int approverId) throws AuthorizationException;

    /**
     * Updates the status of a task.
     * Note that the statuses ids are mapped in 
     * TaskStatus class as public static fields
     *
     * @param taskId    the ID of the task to update
     * @param statusId  the new status ID
     * @param updaterId the ID of the user updating the status
     * @return true if the update was successful, false otherwise
     * @throws AuthorizationException if the updater is not authorized
     */
    boolean updateTaskStatus(int taskId, int statusId, int updaterId) throws AuthorizationException;

    /**
     * Retrieves tasks assigned to a specific employee.
     *
     * @param employeeId the ID of the employee
     * @return a list of Task objects assigned to the employee
     */
    List<Task> getTasksByEmployee(int employeeId);

    /**
     * Retrieves a task by its ID.
     *
     * @param taskId the ID of the task to retrieve
     * @return the Task object, or null if not found
     */
    Task getTaskById(int taskId);

    /**
     * Retrieves all pending tasks.
     * Pending tasks are the tasks created by developers
     * which awaits their teamleader/manager approval
     *
     * @return a list of pending Task objects
     */
    List<Task> getAllPendingTasks();

    /**
     * Retrieves pending tasks for a specific employee.
     * Pending tasks are the tasks created by developers
     * which awaits their teamleader/manager approval
     *
     * @param employeeId the ID of the employee
     * @return a list of pending Task objects for the employee
     */
    List<Task> getPendingTasksForEmployee(int employeeId);

    /**
     * Retrieves all tasks.
     *
     * @return a list of all Task objects
     */
    List<Task> getAllTasks();

    /**
     * Updates a task's information.
     * It selects that task based on the ID attribute in the task object.
     *
     * @param task the Task object with updated information
     * @return true if the update was successful, false otherwise
     */
    boolean updateTask(Task task);
}