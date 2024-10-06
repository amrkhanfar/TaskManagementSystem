package com.managementsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.managementsystem.model.Employee;
import com.managementsystem.model.Task;
import com.managementsystem.util.DatabaseConnection;

public class EmployeeTaskDAO implements EmployeeTaskDAOInterface {

	@Override
	public boolean assignTaskToEmployee(int taskId, int employeeId) {
		String sql = "INSERT INTO employee_task (employee_id, task_id, creation_time) "
				+ "VALUES (?, ?, NOW())";
		boolean isAssigned = false;
		
		try(Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			
			ps.setInt(1, employeeId);
			ps.setInt(2, taskId);
			
			int affectedRows = ps.executeUpdate();
			
			if(affectedRows > 0) {
				isAssigned = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
				
		return isAssigned;
	}

	@Override
	public List<Task> getTasksByEmployeeId(int employeeId) {
		String sql = "Select * FROM tasks t "
				+ "WHERE t.id IN ("
				+ " SELECT task_id FROM employee_task "
				+ "WHERE employee_id = ?)";
		List<Task> tasks = new ArrayList<>();
		
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			
			ps.setInt(1, employeeId);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Task task = TaskDAO.mapResultSetToTask(rs);
				tasks.add(task);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return tasks;
	}

	@Override
	public List<Employee> getEmployeesByTaskId(int taskId) {
		
		String sql = "SELECT * FROM employees e "
				+ "WHERE e.id IN ("
				+ "SELECT employee_id FROM employee_task "
				+ "WHERE task_id = ?)";
		List<Employee> employees = new ArrayList<>();
		
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			
			ps.setInt(1, taskId);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Employee employee = EmployeeDAO.mapResultSetToEmployee(rs);
				employees.add(employee);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return employees;
	}

	@Override
	public boolean removeTaskFromEmployee(int taskId, int employeeId) {
		String sql = "DELETE FROM employee_task WHERE employee_id = ? AND task_id = ?";
        boolean isRemoved = false;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            ps.setInt(2, taskId);

            int affectedRows = ps.executeUpdate();
            isRemoved = affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        return isRemoved;
	}
	

}
