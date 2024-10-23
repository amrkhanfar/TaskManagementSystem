package com.managementsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.managementsystem.model.Task;
import com.managementsystem.model.TaskStatus;
import com.managementsystem.util.DatabaseConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskDAO implements TaskDAOInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskDAO.class);

    @Override
    public int addTask(Task task) {
        LOGGER.debug("Entering addTask with task: {}", task.getTask_title());

        String sql = "INSERT INTO tasks (task_title, task_description, task_status) VALUES (?, ?, ?)";
        int generatedID = 0;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, task.getTask_title());
            ps.setString(2, task.getTask_description());
            ps.setInt(3, task.getTask_status());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                LOGGER.error("Creating task failed, no rows affected.");
                throw new SQLException("Creating task failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedID = generatedKeys.getInt(1);
                    LOGGER.info("Task added successfully with ID: {}", generatedID);
                }
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in addTask: {}", e.getMessage());
        }

        LOGGER.debug("Exiting addTask with generatedID: {}", generatedID);
        return generatedID;
    }

    @Override
    public Task getTaskById(int id) {
        LOGGER.debug("Entering getTaskById with id: {}", id);

        String sql = "SELECT * FROM tasks t WHERE t.id = ?";
        Task foundTask = null;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    foundTask = mapResultSetToTask(rs);
                    LOGGER.debug("Task found: {}", foundTask);
                } else {
                    LOGGER.info("No task found with id: {}", id);
                }
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in getTaskById: {}", e.getMessage());
        }

        LOGGER.debug("Exiting getTaskById");
        return foundTask;
    }

    @Override
    public List<Task> getAllPendingTasks() {
        LOGGER.debug("Entering getAllPendingTasks");

        String sql = "SELECT * FROM tasks t WHERE t.task_status = ?";
        List<Task> tasks = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, TaskStatus.PENDING);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Task task = mapResultSetToTask(rs);
                    tasks.add(task);
                }
                LOGGER.debug("Retrieved {} pending tasks", tasks.size());
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in getAllPendingTasks: {}", e.getMessage());
        }

        LOGGER.debug("Exiting getAllPendingTasks");
        return tasks;
    }

    @Override
    public List<Task> getPendingTasksForEmployee(int employeeId) {
        LOGGER.debug("Entering getPendingTasksForEmployee with employeeId: {}", employeeId);

        String sql = "SELECT t.id, t.task_title, t.task_description, t.task_status FROM tasks t " +
                     "JOIN employee_task et ON et.task_id = t.id " +
                     "WHERE et.employee_id = ? AND t.task_status = ?";
        List<Task> tasks = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            ps.setInt(2, TaskStatus.PENDING);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Task task = mapResultSetToTask(rs);
                    tasks.add(task);
                }
                LOGGER.debug("Retrieved {} pending tasks for employeeId {}", tasks.size(), employeeId);
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in getPendingTasksForEmployee: {}", e.getMessage(), e);
        }

        LOGGER.debug("Exiting getPendingTasksForEmployee");
        return tasks;
    }

    @Override
    public List<Task> getAllTasks() {
        LOGGER.debug("Entering getAllTasks");

        String sql = "SELECT * FROM tasks";
        List<Task> tasks = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Task task = mapResultSetToTask(rs);
                    tasks.add(task);
                }
                LOGGER.debug("Retrieved {} tasks", tasks.size());
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in getAllTasks: {}", e.getMessage());
        }

        LOGGER.debug("Exiting getAllTasks");
        return tasks;
    }

    @Override
    public boolean updateTask(Task task) {
        LOGGER.debug("Entering updateTask with task: {}", task.getTask_title());

        String sql = "UPDATE tasks SET task_title = ?, task_description = ?, task_status = ? WHERE id = ?";
        boolean isUpdated = false;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, task.getTask_title());
            ps.setString(2, task.getTask_description());
            ps.setInt(3, task.getTask_status());
            ps.setInt(4, task.getId());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                isUpdated = true;
                LOGGER.info("Task with id {} updated successfully.", task.getId());
            } else {
                LOGGER.error("No task found with id {}. Update not performed.", task.getId());
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in updateTask: {}", e.getMessage());
        }

        LOGGER.debug("Exiting updateTask with isUpdated: {}", isUpdated);
        return isUpdated;
    }

    @Override
    public boolean deleteTaskById(int id) {
        LOGGER.debug("Entering deleteTaskById with id: {}", id);

        String sql = "DELETE FROM tasks WHERE id = ?";
        boolean isDeleted = false;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                isDeleted = true;
                LOGGER.info("Task with id {} deleted successfully.", id);
            } else {
                LOGGER.error("No task found with id {}. Deletion not performed.", id);
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in deleteTaskById: {}", e.getMessage());
        }

        LOGGER.debug("Exiting deleteTaskById with isDeleted: {}", isDeleted);
        return isDeleted;
    }

    public static Task mapResultSetToTask(ResultSet rs) throws SQLException {
        Task task = new Task(
                rs.getInt("id"),
                rs.getString("task_title"),
                rs.getString("task_description"),
                rs.getInt("task_status")
        );
        return task;
    }
}