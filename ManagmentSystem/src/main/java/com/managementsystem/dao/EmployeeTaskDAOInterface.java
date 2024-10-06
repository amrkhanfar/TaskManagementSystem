package com.managementsystem.dao;

import java.util.List;

import com.managementsystem.model.Employee;
import com.managementsystem.model.Task;

public interface EmployeeTaskDAOInterface {
	
	boolean assignTaskToEmployee(int taskId, int employeeId);
	List<Task> getTasksByEmployeeId(int employeeId);
	List<Employee> getEmployeesByTaskId(int taskId);
	boolean removeTaskFromEmployee(int taskId, int employeeId);
}
