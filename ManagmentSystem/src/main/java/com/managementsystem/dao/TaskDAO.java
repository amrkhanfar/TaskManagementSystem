package com.managementsystem.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.managementsystem.model.Employee;
import com.managementsystem.model.Task;
import com.managementsystem.util.DatabaseConnection;

public class TaskDAO implements TaskDAOInterface {

	@Override
	public int addTask(Task task) {
		String sql = "INSERT INTO tasks (task_title, task_description, task_status) "
				+ "VALUES (?, ?, ?)";
		int generatedID = 0;
		
		try (Connection connection = DatabaseConnection.getInstance().getConnection();
	             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

	            ps.setString(1, task.getTask_title());
	            ps.setString(2, task.getTask_description());
	            ps.setInt(3, task.getTask_status());

	            int affectedRows = ps.executeUpdate();
	            if (affectedRows == 0) {
	                throw new SQLException("Creating task failed, no rows affected.");
	            }
	            
	            try (ResultSet generatedKeys = ps.getGeneratedKeys()){
	            	if(generatedKeys.next()) {
	            		generatedID = generatedKeys.getInt(1);
	            	}
	            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return generatedID;
	}

	@Override
	public Task getTaskById(int id) {
		String sql = "SELECT * FROM tasks t Where t.id = ?";
		Task foundTask = null;
		try(Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			
			ps.setInt(1, id);
			
			try(ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					foundTask = mapResultSetToTask(rs);
				}
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return foundTask;
	}
	
	@Override
	public List<Task> getAllPendingTasks(){
		String sql = "SELECT * FROM tasks t Where t.task_status = ?";
		List<Task> tasks = new ArrayList<>();
		try(Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			
			ps.setInt(1, 1);
			
			try(ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					tasks.add(mapResultSetToTask(rs));
					
				}
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return tasks;
	}
	
	@Override
	public List<Task> getPendingTasksForEmployee(int employeeId) {
	    String sql = "SELECT t.id, t.task_title, t.task_description " +
	                 "FROM employee_task et " +
	                 "JOIN tasks t ON et.task_id = t.id " +
	                 "WHERE et.employee_id = ? AND t.task_status = ?";
	    List<Task> tasks = new ArrayList<>();
	    try (Connection connection = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement ps = connection.prepareStatement(sql)) {

	        ps.setInt(1, employeeId);
	        ps.setInt(2, 1);
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                tasks.add(mapResultSetToTask(rs));
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return tasks;
	}
	
	@Override
	public List<Task> getAllTasks() {
		String sql = "SELECT * FROM tasks";
		List<Task> tasks = new ArrayList<>();
		
		try(Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			
			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()) {
					Task task = mapResultSetToTask(rs);
					tasks.add(task);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	@Override
	public boolean updateTask(Task task) {
		String sql = "UPDATE tasks SET task_title = ?, task_description = ?, task_status = ? "
				+ "WHERE id = ?";
		boolean isUpdated = false;
		
		try(Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			
			ps.setString(1, task.getTask_title());
			ps.setString(2, task.getTask_description());
			ps.setInt(3, task.getTask_status());
			ps.setInt(4, task.getId());
			
			int affectedRows = ps.executeUpdate();
			
			if(affectedRows > 0) {
				isUpdated = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isUpdated;
	}

	@Override
	public boolean deleteTaskById(int id) {
		String sql = "DELETE FROM tasks WHERE id = ?";
		boolean isDeleted = false;
		
		try(Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			
			ps.setInt(1, id);
			
			int affectedRows = ps.executeUpdate();
			
			if(affectedRows > 0) {
				isDeleted = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isDeleted;
	}
	
	public static Task mapResultSetToTask(ResultSet rs) throws SQLException {
		Task task = new Task(rs.getInt("id"), rs.getString("task_title"), rs.getString("task_description"), rs.getInt("task_status"));
		return task;
	}

}
