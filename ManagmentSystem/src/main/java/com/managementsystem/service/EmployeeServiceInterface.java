package com.managementsystem.service;

import java.util.List;

import com.managementsystem.exception.AuthenticationException;
import com.managementsystem.exception.AuthorizationException;
import com.managementsystem.model.Employee;

public interface EmployeeServiceInterface {
	Employee authenticate(String email, String password) throws AuthenticationException;
	int addEmployee(Employee employee, int creatorId) throws AuthorizationException;
	Employee getEmployeeById(int employeeId);
	List<Employee> getAllEmployees();
	List<Employee> getEmployeesByTeam(int teamId);
	boolean updateEmployee(Employee employee, int updaterId) throws AuthorizationException;
	boolean deleteEmployee(int employeeId, int deleterId) throws AuthorizationException;
}
