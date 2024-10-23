package com.managementsystem.dao;

import java.util.List;

import com.managementsystem.model.Employee;

/**
 * EmployeeDAOInterface - Interface defining data access methods for Employee entities.
 * Methods access the database in singleton pattern. Each method obtains a connection.
 * 
 * @author Amr
 * @version Oct 7, 2024
 */
public interface EmployeeDAOInterface {

    /**
     * Adds a new Employee to the database.
     * Note that the ID attribute will be discarded since 
     * the database will generate a unique one.
     *
     * @param employee the Employee object to be added.
     * @return the generated ID of the newly added employee.
     */
    int addEmployee(Employee employee);

    /**
     * Retrieves an Employee by their ID.
     *
     * @param id the ID of the employee.
     * @return the Employee object with the specified ID, or Null if not found,
     */
    Employee getEmployeeById(int id);

    /**
     * Retrieves an Employee by their email address.
     *
     * @param email the email address of the employee.
     * @return the Employee object with the specified email, or Null if not found.
     */
    Employee getEmployeeByEmail(String email);

    /**
     * Retrieves a list of Employees with the specified role ID.
     * Not that the roles IDs are mapped in Role.java class as public static fields.
     *
     * @param roleId the role ID of the employees.
     * @return a list of Employees with the specified role ID.
     */
    List<Employee> getEmployeesByRole(int roleId);

    /**
     * Retrieves a list of Employees who are members the specified team.
     *
     * @param teamID the ID of the team.
     * @return a list of Employees in the specified team.
     */
    List<Employee> getEmployeesByTeamID(int teamID);

    /**
     * Retrieves a list of all Employees.
     *
     * @return a list of all Employees.
     */
    List<Employee> getAllEmplyees();

    /**
     * Updates the information of an existing Employee.
     * The method will update the employee based on the ID attribute in Employee object.
     * 
     * @param employee the Employee object with updated information.
     * @return true if the update was successful (rows get affected), false otherwise.
     */
    Boolean updateEmployee(Employee employee);

    /**
     * Deletes an Employee by their ID.
     *
     * @param id the ID of the employee to delete
     * @return true if the deletion was successful (rows get affected), false otherwise.
     */
    boolean deleteEmployeeById(int id);
}