package com.managementsystem.service;

import java.util.List;
import com.managementsystem.exception.AuthenticationException;
import com.managementsystem.exception.AuthorizationException;
import com.managementsystem.model.Employee;

/**
 * Interface defining the service methods for Employee operations.
 * 
 * @author Amr
 * @version Oct 8, 2024
 */
public interface EmployeeServiceInterface {

    /**
     * Authenticates an employee using their email and password.
     *
     * @param email    the email of the employee
     * @param password the password provided
     * @return the authenticated Employee object
     * @throws AuthenticationException if authentication fails
     */
    Employee authenticate(String email, String password) throws AuthenticationException;

    /**
     * Adds a new employee to the system.
     *
     * @param employee  the Employee object to add
     * @param creatorId the ID of the user creating the employee
     * @return the generated ID of the new employee
     * @throws AuthorizationException if the creator is not authorized
     */
    int addEmployee(Employee employee, int creatorId) throws AuthorizationException;

    /**
     * Retrieves an employee by their ID.
     *
     * @param employeeId the ID of the employee to retrieve
     * @return the Employee object, Null if not found
     */
    Employee getEmployeeById(int employeeId);

    /**
     * Retrieves a list of all employees.
     *
     * @return a list of Employee objects
     */
    List<Employee> getAllEmployees();

    /**
     * Retrieves employees who are part of a specific team.
     *
     * @param teamId the ID of the team
     * @return a list of Employee objects in the team
     */
    List<Employee> getEmployeesByTeam(int teamId);

    /**
     * Updates an employee's information.
     *
     * @param employee  the Employee object with updated information
     * @param updaterId the ID of the user performing the update
     * @return True if the update was successful, false otherwise
     * @throws AuthorizationException if the updater is not authorized
     */
    boolean updateEmployee(Employee employee, int updaterId) throws AuthorizationException;

    /**
     * Deletes an employee by their ID.
     *
     * @param employeeId the ID of the employee to delete
     * @param deleterId  the ID of the user performing the deletion
     * @return true if the deletion was successful, false otherwise
     * @throws AuthorizationException if the deleter is not authorized
     */
    boolean deleteEmployee(int employeeId, int deleterId) throws AuthorizationException;
}