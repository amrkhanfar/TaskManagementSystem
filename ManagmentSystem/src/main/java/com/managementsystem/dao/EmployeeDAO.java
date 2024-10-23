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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmployeeDAO implements EmployeeDAOInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDAO.class);

    @Override
    public int addEmployee(Employee employee) {
        LOGGER.debug("Entering addEmployee with employee: {}", employee);

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
                LOGGER.error("Adding new employee failed. No rows affected.");
                throw new SQLException("Adding new employee failed. No rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedID = rs.getInt(1);
                    LOGGER.info("Employee added successfully with ID: {}", generatedID);
                }
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in addEmployee: {}", e.getMessage());
        }

        LOGGER.debug("Exiting addEmployee with generatedID: {}", generatedID);
        return generatedID;
    }

    @Override
    public Employee getEmployeeById(int id) {
        LOGGER.debug("Entering getEmployeeById with id: {}", id);

        String sql = "SELECT * FROM employees e WHERE e.id = ?";
        Employee foundEmployee = null;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    foundEmployee = mapResultSetToEmployee(rs);
                    LOGGER.debug("Employee found: {}", foundEmployee);
                } else {
                    LOGGER.warn("No employee found with id: {}", id);
                }
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in getEmployeeById: {}", e.getMessage());
        }

        LOGGER.debug("Exiting getEmployeeById");
        return foundEmployee;
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        LOGGER.debug("Quering getEmployeeByEmail with email: {}", email);

        String sql = "SELECT * FROM employees e WHERE e.email = ?";
        Employee employee = null;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    employee = mapResultSetToEmployee(rs);
                    LOGGER.debug("Employee found: {}", employee);
                } else {
                    LOGGER.info("No employee found with email: {}", email);
                }
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in getEmployeeByEmail: {}", e.getMessage());
        }

        LOGGER.debug("Exiting getEmployeeByEmail");
        return employee;
    }

    @Override
    public List<Employee> getEmployeesByRole(int roleId) {
        LOGGER.debug("Entering getEmployeesByRole with roleId: {}", roleId);

        String sql = "SELECT * FROM employees e WHERE e.role_id = ?";
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, roleId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee employee = mapResultSetToEmployee(rs);
                    employees.add(employee);
                }
                LOGGER.debug("Found {} employees with roleId {}", employees.size(), roleId);
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in getEmployeesByRole: {}", e.getMessage());
        }

        LOGGER.debug("Exiting getEmployeesByRole");
        return employees;
    }

    @Override
    public List<Employee> getEmployeesByTeamID(int teamID) {
        LOGGER.debug("Entering getEmployeesByTeamID with teamID: {}", teamID);

        String sql = "SELECT * FROM employees e WHERE e.team_id = ?";
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, teamID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee employee = mapResultSetToEmployee(rs);
                    employees.add(employee);
                }
                LOGGER.debug("Found {} employees in teamID {}", employees.size(), teamID);
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in getEmployeesByTeamID: {}", e.getMessage());
        }

        LOGGER.debug("Exiting getEmployeesByTeamID");
        return employees;
    }

    @Override
    public List<Employee> getAllEmplyees() {
        LOGGER.debug("Entering getAllEmployees");

        String sql = "SELECT * FROM employees";
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee employee = mapResultSetToEmployee(rs);
                    employees.add(employee);
                }
                LOGGER.debug("Retrieved {} employees", employees.size());
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in getAllEmployees: {}", e.getMessage());
        }

        LOGGER.debug("Exiting getAllEmployees");
        return employees;
    }

    @Override
    public Boolean updateEmployee(Employee employee) {
        LOGGER.debug("Entering updateEmployee with employee: {}", employee);

        String sql = "UPDATE employees SET name = ?, email = ?, role_id = ?, team_id = ?, password = ? WHERE id = ?";
        boolean isUpdated = false;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, employee.getName());
            ps.setString(2, employee.getEmail());
            ps.setInt(3, employee.getRole_id());
            ps.setInt(4, employee.getTeam_id());
            ps.setString(5, employee.getPassword());
            ps.setInt(6, employee.getId());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                isUpdated = true;
                LOGGER.info("Employee with id {} updated successfully.", employee.getId());
            } else {
                LOGGER.warn("No employee found with id {}. Update not performed.", employee.getId());
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in updateEmployee: {}", e.getMessage());
        }

        LOGGER.debug("Exiting updateEmployee with isUpdated: {}", isUpdated);
        return isUpdated;
    }

    @Override
    public boolean deleteEmployeeById(int id) {
        LOGGER.debug("Entering deleteEmployeeById with id: {}", id);

        String sql = "DELETE FROM employees WHERE id = ?";
        boolean isDeleted = false;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                isDeleted = true;
                LOGGER.info("Employee with id {} deleted successfully.", id);
            } else {
                LOGGER.warn("No employee found with id {}. Deletion not performed.", id);
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in deleteEmployeeById: {}", e.getMessage());
        }

        LOGGER.debug("Exiting deleteEmployeeById with isDeleted: {}", isDeleted);
        return isDeleted;
    }

    public static Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getInt("role_id"),
                rs.getInt("team_id"),
                rs.getString("password")
        );
        return employee;
    }
}