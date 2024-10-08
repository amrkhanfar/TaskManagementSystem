package com.managementsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.managementsystem.model.TaskStatus;
import com.managementsystem.util.DatabaseConnection;

public class TaskStatusDAO implements TaskStatusDAOInterface {

    @Override
    public List<TaskStatus> getAllStatuses() {
	String sql = "SELECT * FROM task_status";
	List<TaskStatus> statuses = new ArrayList<>();

	try (Connection connection = DatabaseConnection.getInstance().getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery()) {

	    while (rs.next()) {
		TaskStatus taskStatus = mapResultSetToTaskStatus(rs);
		statuses.add(taskStatus);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return statuses;
    }

    private TaskStatus mapResultSetToTaskStatus(ResultSet rs) throws SQLException {
	TaskStatus taskStatus = new TaskStatus(rs.getInt("id"), rs.getString("status_name"));
	return taskStatus;
    }
}
