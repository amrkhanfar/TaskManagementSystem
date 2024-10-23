package com.managementsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.managementsystem.model.TaskStatus;
import com.managementsystem.util.DatabaseConnection;

public class TaskStatusDAO implements TaskStatusDAOInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskStatusDAO.class);

    @Override
    public List<TaskStatus> getAllStatuses() {
	LOGGER.debug("Entering getAllStatuses");
	
	String sql = "SELECT * FROM task_status";
	List<TaskStatus> statuses = new ArrayList<>();

	try (Connection connection = DatabaseConnection.getInstance().getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery()) {

	    while (rs.next()) {
		TaskStatus taskStatus = mapResultSetToTaskStatus(rs);
		statuses.add(taskStatus);
	    }
	    LOGGER.debug("Retreived {} task statuses", statuses.size());
	} catch (SQLException e) {
	    LOGGER.error("SQLException in getAllStatuses: {}", e.getMessage());
	}
	return statuses;
    }

    private TaskStatus mapResultSetToTaskStatus(ResultSet rs) throws SQLException {
	TaskStatus taskStatus = new TaskStatus(rs.getInt("id"), rs.getString("status_name"));
	return taskStatus;
    }
}
