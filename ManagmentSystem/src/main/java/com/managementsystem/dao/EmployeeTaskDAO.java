package com.managementsystem.dao;

import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.managementsystem.model.Employee;
import com.managementsystem.model.Task;
import com.managementsystem.util.DatabaseConnection;

public class EmployeeTaskDAO implements EmployeeTaskDAOInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeTaskDAO.class);

    @Override
    public boolean assignTaskToEmployee(int taskId, int employeeId) {
        LOGGER.debug("Entering assignTaskToEmployee with taskId: {}, employeeId: {}", taskId, employeeId);

        String sql = "INSERT INTO employee_task (employee_id, task_id, creation_time) VALUES (?, ?, NOW())";
        boolean isAssigned = false;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            ps.setInt(2, taskId);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                isAssigned = true;
                LOGGER.info("Task {} assigned to employee {}", taskId, employeeId);
            } else {
                LOGGER.warn("Failed to assign task {} to employee {}", taskId, employeeId);
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in assignTaskToEmployee: {}", e.getMessage());
        }

        LOGGER.debug("Exiting assignTaskToEmployee with isAssigned: {}", isAssigned);
        return isAssigned;
    }

    @Override
    public List<Task> getTasksByEmployeeId(int employeeId) {
        LOGGER.debug("Entering getTasksByEmployeeId with employeeId: {}", employeeId);

        String sql = "SELECT * FROM tasks t WHERE t.id IN (SELECT task_id FROM employee_task WHERE employee_id = ?)";
        List<Task> tasks = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, employeeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Task task = TaskDAO.mapResultSetToTask(rs);
                    tasks.add(task);
                }
                LOGGER.debug("Found {} tasks for employeeId {}", tasks.size(), employeeId);
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in getTasksByEmployeeId: {}", e.getMessage());
        }

        LOGGER.debug("Exiting getTasksByEmployeeId");
        return tasks;
    }

    @Override
    public Employee getEmployeeByTaskId(int taskId) {
        LOGGER.debug("Entering getEmployeeByTaskId with taskId: {}", taskId);

        String sql = "SELECT * FROM employees e WHERE e.id IN (SELECT employee_id FROM employee_task WHERE task_id = ?)";
        Employee employee = null;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, taskId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    employee = EmployeeDAO.mapResultSetToEmployee(rs);
                    LOGGER.debug("Employee found for taskId {}: {}", taskId, employee);
                } else {
                    LOGGER.info("No employee found for taskId {}", taskId);
                }
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in getEmployeeByTaskId: {}", e.getMessage());
        }

        LOGGER.debug("Exiting getEmployeeByTaskId");
        return employee;
    }

    @Override
    public boolean removeTaskFromEmployee(int taskId, int employeeId) {
        LOGGER.debug("Entering removeTaskFromEmployee with taskId: {}, employeeId: {}", taskId, employeeId);

        String sql = "DELETE FROM employee_task WHERE employee_id = ? AND task_id = ?";
        boolean isRemoved = false;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            ps.setInt(2, taskId);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                isRemoved = true;
                LOGGER.info("Task {} removed from employee {}", taskId, employeeId);
            } else {
                LOGGER.warn("No assignment found for task {} and employee {}", taskId, employeeId);
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in removeTaskFromEmployee: {}", e.getMessage());
        }

        LOGGER.debug("Exiting removeTaskFromEmployee with isRemoved: {}", isRemoved);
        return isRemoved;
    }
}