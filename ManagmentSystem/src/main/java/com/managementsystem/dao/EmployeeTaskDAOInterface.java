package com.managementsystem.dao;

import java.util.List;

import com.managementsystem.model.Employee;
import com.managementsystem.model.Task;

/**
 * 
 * EmployeeTaskDAOInterface - Interface defining data access methods
 * for managing tasks assignments to employees.
 *
 * @author Amr
 * @version Oct 7, 2024
 */
public interface EmployeeTaskDAOInterface {

    /**
     * Assigns a Task to an Employee.
     *
     * @param taskId     the ID of the task to assign.
     * @param employeeId the ID of the employee to assign the task to.
     * @return true if the task was successfully assigned (rows get affected), false otherwise
     */
    boolean assignTaskToEmployee(int taskId, int employeeId);

    /**
     * Retrieves a list of Tasks assigned to the specified Employee.
     *
     * @param employeeId the ID of the employee.
     * @return a list of Tasks assigned to the employee.
     */
    List<Task> getTasksByEmployeeId(int employeeId);

    /**
     * Retrieves a list of Employees assigned to the specified Task.
     *
     * @param taskId the ID of the task.
     * @return a list of Employees assigned to the task.
     */
    List<Employee> getEmployeesByTaskId(int taskId);

    /**
     * Removes a Task assignment from an Employee.
     *
     * @param taskId     the ID of the task to un-assign.
     * @param employeeId the ID of the employee.
     * @return true if the task was successfully un-assigned from the employee (rows get affected).
     */
    boolean removeTaskFromEmployee(int taskId, int employeeId);
}

