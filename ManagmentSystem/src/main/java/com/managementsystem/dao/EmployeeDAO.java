package com.managementsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.managementsystem.model.Employee;
import com.managementsystem.util.DatabaseConnection;

public class EmployeeDAO implements EmployeeDAOInterface {

	@Override
	public int addEmployee(Employee employee) {

		String sql = "INSERT INTO employees (name, email, role_id, team_id, password) VALUES (?, ?, ?, ?, ?)";
		int generatedID = 0;
		try (Connection connection = DatabaseConnection.getInstance().getConnection(); 
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			
			ps.setString(1, employee.getName());
			ps.setString(2, employee.getEmail());
			ps.setInt(3, employee.getRole_id());
			ps.setInt(4, employee.getTeam_id());
			ps.setString(5, employee.getPassword());
			
			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Adding new employee failed. No rows affected.");
			}
			
			generatedID = ps.getGeneratedKeys().getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return generatedID;
	}

	@Override
	public Employee getEmployeeById(int id) {
		
		String sql = "SELECT * FROM employees e Where e.id = ?";
		Employee foundEmployee = null;
		try(Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			
			ps.setInt(1, id);
			
			try(ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					foundEmployee = mapResultSetToEmployee(rs);
				}
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return foundEmployee;
	}

	@Override
	public Employee getEmployeeByEmail(String email) {
		
		String sql = "SELECT * from employees e "
				+ "WHERE e.email = ? ";
		Employee employee = null;
		
		try(Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			
			ps.setString(1, email);
			
			try(ResultSet rs = ps.executeQuery()) {
				if(rs.next()) {
					employee = mapResultSetToEmployee(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employee;
	}

	@Override
	public List<Employee> getEmployeesByRole(int roleId) {

		String sql = "SELECT * FROM employees e "
				+ "WHERE e.role_id = ?";
		List<Employee> employees = new ArrayList<>();
		
		try(Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			
			ps.setInt(1, roleId);
			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()) {
					Employee employee = mapResultSetToEmployee(rs);
					employees.add(employee);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employees;
	}

	@Override
	public List<Employee> getEmployeesByTeamID(int teamID) {
		String sql = "SELECT * FROM employees e "
				+ "WHERE e.team_id = ?";
		List<Employee> employees = new ArrayList<>();
		
		try(Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			
			ps.setInt(1, teamID);
			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()) {
					Employee employee = mapResultSetToEmployee(rs);
					employees.add(employee);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employees;
	}

	@Override
	public List<Employee> getAllEmplyees() {
		String sql = "SELECT * FROM employees";
		List<Employee> employees = new ArrayList<>();
		
		try(Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			
			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()) {
					Employee employee = mapResultSetToEmployee(rs);
					employees.add(employee);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employees;
	}

	@Override
	public Boolean updateEmployee(Employee employee) {
		String sql = "UPDATE employees SET name = ?, email = ?, "
				+ "role_id = ?, team_id = ?, password = ? "
				+ "WHERE id = ?";
		boolean isUpdated = false;
		
		try(Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)){
			
			ps.setString(1, employee.getName());
			ps.setString(2, employee.getEmail());
            ps.setInt(3, employee.getRole_id());
            ps.setInt(4, employee.getTeam_id());
            ps.setString(5, employee.getPassword());
            ps.setInt(6, employee.getId());
            
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
	public boolean deleteEmployeeById(int id) {
		String sql = "DELETE FROM employees WHERE id = ?";
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
	
	public static Employee mapResultSetToEmployee(ResultSet rs) throws SQLException{
		Employee employee = new Employee(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getInt("role_id"), rs.getInt("team_id"), rs.getString("password"));
        return employee;
		
	}
}
