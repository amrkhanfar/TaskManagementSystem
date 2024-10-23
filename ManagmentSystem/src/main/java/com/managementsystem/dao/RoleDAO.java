package com.managementsystem.dao;

import java.sql.Connection; 

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.managementsystem.model.Role;
import com.managementsystem.util.DatabaseConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoleDAO implements RoleDAOInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleDAO.class);

    @Override
    public List<Role> getAllRoles() {
	LOGGER.debug("Entring getAllRoles");
	String sql = "SELECT * FROM roles";
	List<Role> roles = new ArrayList<>();

	try (Connection connection = DatabaseConnection.getInstance().getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery()) {

	    while (rs.next()) {
		Role role = mapResultSetToRole(rs);
		roles.add(role);
	    }
	    LOGGER.debug("Retreived {} roles", roles.size());
	} catch (SQLException e) {
	    LOGGER.error("SQLException in getAllRoles: {}", e.getMessage());
	}
	LOGGER.debug("Exiting getAllRoles");
	return roles;
    }

    @Override
    public Role getRoleById(int id) {
	LOGGER.debug("Entering getRoleById with id: {}", id);
	
	String sql = "SELECT * FROM roles WHERE id = ?";
	Role role = null;

	try (Connection connection = DatabaseConnection.getInstance().getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql)) {
	    pstmt.setInt(1, id);
	    try (ResultSet rs = pstmt.executeQuery()) {
		if (rs.next()) {
		    role = mapResultSetToRole(rs);
		    LOGGER.debug("Role found: {}", role);
		} else {
		    LOGGER.info("No role found with the id: {}", id);
		}
	    }
	} catch (SQLException e) {
	    LOGGER.error("SQLException in getRoleById: {}", e.getMessage());

	}
	LOGGER.debug("Exiting getRoleById");
	return role;
    }

    @Override
    public Role getRoleByName(String name) {
	LOGGER.debug("Entring getRoleByName with name: {}", name);
	
	String sql = "SELECT * FROM roles WHERE name = ?";
	Role role = null;

	try (Connection connection = DatabaseConnection.getInstance().getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql)) {
	    pstmt.setString(1, name);
	    try (ResultSet rs = pstmt.executeQuery()) {
		if (rs.next()) {
		    role = mapResultSetToRole(rs);
		    LOGGER.debug("Role found: {}", role);
		} else {
		    LOGGER.info("No role found with name: {}", name);
		}
	    }
	} catch (SQLException e) {
	    LOGGER.error("SQLException in getRoleByName: {}", e.getMessage());
	}
	return role;
    }

    private Role mapResultSetToRole(ResultSet rs) throws SQLException {
	Role role = new Role(rs.getInt("id"), rs.getString("role_name"));
	return role;
    }
}
