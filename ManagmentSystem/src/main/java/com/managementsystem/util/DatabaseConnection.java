package com.managementsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    
    private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseConnection.class);

    private DatabaseConnection() {
	try {
	    Class.forName("com.mysql.cj.jdbc.Driver");
	} catch (ClassNotFoundException e) {
	    LOGGER.warn("Error loading MYSQL Driver: {}", e.getMessage());
	}
    }

    public static DatabaseConnection getInstance() {
	if (instance == null) {
	    LOGGER.info("Created a databaseConnection instance");
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
