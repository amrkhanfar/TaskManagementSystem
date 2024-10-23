package com.managementsystem.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.managementsystem.dao.EmployeeDAO;
import com.managementsystem.exception.AuthenticationException;
import com.managementsystem.exception.AuthorizationException;
import com.managementsystem.model.Employee;
import com.managementsystem.model.Role;

public class EmployeeService implements EmployeeServiceInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);
    
    EmployeeDAO employeeDAO = new EmployeeDAO();

    @Override
    public Employee authenticate(String email, String password) throws AuthenticationException {
	LOGGER.debug("Entring authenticate with email: {}", email);
	Employee employee = employeeDAO.getEmployeeByEmail(email);

	if (employee == null) {
	    LOGGER.warn("Authentication failed: no account found for email: {}", email);
	    throw new AuthenticationException("No account is registered with the entered email");
	}

	boolean passwordMatch = employee.getPassword().equals(password);
	if (!passwordMatch) {
	    LOGGER.warn("Authentication failed for email {}: Incorrect password", email);
	    throw new AuthenticationException("Incorrect password");
	}
	
	LOGGER.info("User {} authenticated successfully.", email);
	LOGGER.debug("Exiting authenticate with employee: {}", employee);
	return employee;
    }

    @Override
    public int addEmployee(Employee employee, int creatorId) throws AuthorizationException {
	LOGGER.debug("Entering addEmployee with employee: {} and creatorId: {}", employee, creatorId);
	
	Employee creator = employeeDAO.getEmployeeById(creatorId);
	if (creator == null) {
	    LOGGER.warn("Adding employee failed: creator with id: {} not found", creatorId);
	    throw new AuthorizationException("Creator not found");
	}
	if (creator.getRole_id() != Role.MANAGER) {
	    LOGGER.warn("Adding employee failed: creator with id: {} is not a manger", creatorId);
	    throw new AuthorizationException("Only manager have access to add new employees");
	}

	int generatedId = employeeDAO.addEmployee(employee);
	LOGGER.info("Employee: {} is added successfuly with generated id: {}", employee, generatedId);
	LOGGER.debug("Exiting addEmployee with generatedId: {}", generatedId);
	return generatedId;
    }

    @Override
    public Employee getEmployeeById(int employeeId) {
	LOGGER.debug("Entering addEmployee with emplyoeeid: {}", employeeId);
	Employee employeeById = employeeDAO.getEmployeeById(employeeId);
	LOGGER.debug("Exiting addEmployee with employee: {}", employeeById);
	return employeeById;
    }

    @Override
    public List<Employee> getAllEmployees() {
	LOGGER.debug("Entering getAllEmployees");
	List<Employee> allEmplyees = employeeDAO.getAllEmplyees();
	LOGGER.debug("Exiting getAllEmployees");
	return allEmplyees;
    }

    @Override
    public List<Employee> getEmployeesByTeam(int teamId) {
	LOGGER.debug("Entering getEmployeesByTeam with teamId: {}", teamId);
	List<Employee> employeesByTeamID = employeeDAO.getEmployeesByTeamID(teamId);
	LOGGER.debug("Exiting getEmployeesByTeam");
	return employeesByTeamID;
    }

    @Override
    public boolean updateEmployee(Employee employee, int updaterId) throws AuthorizationException {
	LOGGER.debug("Entering updateEmployee with employee: {} and updaterId: {}", employee, updaterId);
	
	Employee updater = employeeDAO.getEmployeeById(updaterId);
	if (updater == null) {
	    LOGGER.warn("Updating employee failed: updater with id: {} not found", updaterId);
	    throw new AuthorizationException("Updater not found");
	}
	if (updater.getRole_id() != Role.MANAGER) {
	    LOGGER.warn("Updating employee failed: updated with id: {} is not a manager", updaterId);
	    throw new AuthorizationException("Only managers have access to update employees' information");
	}

	Employee updated = employeeDAO.getEmployeeById(employee.getId());
	if (updated == null) {
	    LOGGER.warn("Updating employee failed: employee to update with id: {} not found", employee.getId());
	    throw new IllegalArgumentException("Employee to update is not found.");
	}
	
	Boolean isUpdated = employeeDAO.updateEmployee(employee);
	
	LOGGER.info("Employee with id: {} is updated succefully", employee.getId());
	LOGGER.debug("Exting update employee with isUpdated: ", isUpdated);
	
	return isUpdated;
    }

    @Override
    public boolean deleteEmployee(int employeeId, int deleterId) throws AuthorizationException {
	LOGGER.debug("Entering deleteEmployee with employeeId: {} and deleterId: {}", employeeId, deleterId);
	
	Employee deleter = employeeDAO.getEmployeeById(deleterId);
	if (deleter == null) {
	    LOGGER.warn("Deleting employee failed: deleter with id: {} not found", deleterId);
	    throw new AuthorizationException("Deleter not found.");
	}
	if (deleter.getRole_id() != Role.MANAGER) {
	    LOGGER.warn("Deleting employee failed: deleter with id: {} is not a manager", deleterId);
	    throw new AuthorizationException("Only Managers can delete employees.");
	}

	Employee employeeToDelete = employeeDAO.getEmployeeById(employeeId);
	if (employeeToDelete == null) {
	    LOGGER.warn("Deleting employee failed: employee to delete with id: {} not found", employeeId);
	    throw new IllegalArgumentException("Employee to delete is not found");
	}
	
	boolean deleteEmployeeById = employeeDAO.deleteEmployeeById(employeeId);
	
	LOGGER.info("Employee with id: {} has been deleted succefully", employeeId);
	LOGGER.debug("Exting update employee with isUpdated: ", deleteEmployeeById);
	
	return deleteEmployeeById;
    }
}
