package com.managementsystem.dao;
import java.util.List;

import com.managementsystem.model.Employee;

public interface EmployeeDAOInterface {
	
	int addEmployee(Employee employee);
	Employee getEmployeeById(int id);
	Employee getEmployeeByEmail(String email);
	List<Employee> getEmployeesByRole(int roleId);
	List<Employee> getEmployeesByTeamID(int teamID);
	List<Employee> getAllEmplyees();
	Boolean updateEmployee(Employee employee);
	boolean deleteEmployeeById(int id);
}