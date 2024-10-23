package com.managementsystem.dao;

import java.util.List;

import com.managementsystem.model.Task;

/**
 * 
 * TaskDAOInterface - Interface defining data access methods for Task entities.
 *
 * @author Amr
 * @version Oct 7, 2024
 */
public interface TaskDAOInterface { 
    /**
     * Adds a new Task to the database.
     * Note that the ID attribute will be discarded since 
     * the database will generate a unique one.
     *
     * @param task the Task object to be added.
     * @return the generated ID of the newly added task.
     */
    int addTask(Task task);

    /**
     * Retrieves a Task by its ID.
     *
     * @param id the unique ID of the task.
     * @return the Task object with the specified ID, or {@code null} if not found.
     */
    Task getTaskById(int id);

    /**
     * Retrieves a list of all pending Tasks.
     *
     * @return a list of all pending Tasks.
     */
    List<Task> getAllPendingTasks();

    /**
     * Retrieves a list of pending Tasks assigned to a specific Employee.
     *
     * @param employeeId the unique ID of the employee
     * @return a list of pending Tasks for the employee.
     */
    List<Task> getPendingTasksForEmployee(int employeeId);

    /**
     * Retrieves a list of all Tasks.
     *
     * @return a list of all Tasks.
     */
    List<Task> getAllTasks();

    /**
     * Updates the information of an existing Task.
     * The method will update the employee based on the ID attribute in Employee object.
     * 
     * @param task the Task object with updated information.
     * @return {@code true} if the update was successful, {@code false} otherwise,
     */
    boolean updateTask(Task task);

    /**
     * Deletes a Task by its ID.
     *
     * @param id the ID of the task to delete.
     * @return true if the deletion was successful (rows get affected), false otherwise.
     */
    boolean deleteTaskById(int id);
}