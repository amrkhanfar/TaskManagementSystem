package com.managementsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
	try {
	    Class.forName("com.mysql.cj.jdbc.Driver");
	} catch (ClassNotFoundException e) {
	    throw new RuntimeException("Error loading MySQL Driver", e);
	}
    }

    public static DatabaseConnection getInstance() {
	if (instance == null) {
	    instance = new DatabaseConnection();
	}
	return instance;
    }

    public Connection getConnection() throws SQLException {
	return DriverManager.getConnection("jdbc:mysql://localhost:3306/employeemanagmentsystem", "root", "");
    }

    public void closeConnection() throws SQLException {
	connection.close();
    }
}
