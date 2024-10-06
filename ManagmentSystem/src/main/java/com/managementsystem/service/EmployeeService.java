package com.managementsystem.service;

import java.util.List;

import com.managementsystem.dao.EmployeeDAO;
import com.managementsystem.exception.AuthenticationException;
import com.managementsystem.exception.AuthorizationException;
import com.managementsystem.model.Employee;
import com.managementsystem.model.Role;

public class EmployeeService implements EmployeeServiceInterface {
	
	EmployeeDAO employeeDAO = new EmployeeDAO();

	@Override
	public Employee authenticate(String email, String password) throws AuthenticationException {
		
		Employee employee = employeeDAO.getEmployeeByEmail(email);
		
		if (employee == null) {
			throw new AuthenticationException("No account is registered with the entered email");
		}
		
		boolean passwordMatch = employee.getPassword().equals(password);
		if(!passwordMatch) {
			throw new AuthenticationException("Incorrect password");
		}
		
		
		return employee;
	}

	@Override
	public int addEmployee(Employee employee, int creatorId) throws AuthorizationException {
		
		Employee creator = employeeDAO.getEmployeeById(creatorId);
		
		if(creator == null) {
			throw new AuthorizationException("Creator not found");
		}
		
		if(creator.getRole_id() != Role.MANAGER) {
			throw new AuthorizationException("Only manager have access to add new employees");
		}
		
		int generatedId = employeeDAO.addEmployee(employee);
		return generatedId;

	}

	@Override
	public Employee getEmployeeById(int employeeId) {
		// TODO Auto-generated method stub
		return employeeDAO.getEmployeeById(employeeId);
	}

	@Override
	public List<Employee> getAllEmployees() {
		// TODO Auto-generated method stub
		return employeeDAO.getAllEmplyees();
	}

	@Override
	public List<Employee> getEmployeesByTeam(int teamId) {
		// TODO Auto-generated method stub
		return employeeDAO.getEmployeesByTeamID(teamId);
	}

	@Override
	public boolean updateEmployee(Employee employee, int updaterId) throws AuthorizationException {
		
		Employee updater = employeeDAO.getEmployeeById(updaterId);
		if(updater == null) {
			throw new AuthorizationException("Updater not found");
		}
		
		if(updater.getRole_id() != Role.MANAGER) {
			throw new AuthorizationException("Only managers have access to update employees' information");
		}

		Employee updated = employeeDAO.getEmployeeById(employee.getId());
		if(updated == null) {
			throw new IllegalArgumentException("Employee to update is not found.");
		}
		
		return employeeDAO.updateEmployee(employee);
	}

	@Override
	public boolean deleteEmployee(int employeeId, int deleterId) throws AuthorizationException {
		
		Employee deleter = employeeDAO.getEmployeeById(deleterId);
        if (deleter == null) {
            throw new AuthorizationException("Deleter not found.");
        }
        if (deleter.getRole_id() != Role.MANAGER) {
            throw new AuthorizationException("Only Managers can delete employees.");
        }
        
        
        Employee employeeToDelete = employeeDAO.getEmployeeById(employeeId);
        if(employeeToDelete == null) {
        	throw new IllegalArgumentException("Employee to delete is not found");
        }
        
        
		return employeeDAO.deleteEmployeeById(employeeId);
	}
	
	
		
	}

