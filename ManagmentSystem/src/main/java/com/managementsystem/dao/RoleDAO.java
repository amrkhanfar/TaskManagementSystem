package com.managementsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.managementsystem.model.Role;
import com.managementsystem.util.DatabaseConnection;

public class RoleDAO implements RoleDAOInterface {

    @Override
    public List<Role> getAllRoles() {
	String sql = "SELECT * FROM roles";
	List<Role> roles = new ArrayList<>();

	try (Connection connection = DatabaseConnection.getInstance().getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery()) {

	    while (rs.next()) {
		Role role = mapResultSetToRole(rs);
		roles.add(role);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return roles;
    }

    @Override
    public Role getRoleById(int id) {
	String sql = "SELECT * FROM roles WHERE id = ?";
	Role role = null;

	try (Connection connection = DatabaseConnection.getInstance().getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql)) {
	    pstmt.setInt(1, id);
	    try (ResultSet rs = pstmt.executeQuery()) {
		if (rs.next()) {
		    role = mapResultSetToRole(rs);
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();

	}
	return role;
    }

    @Override
    public Role getRoleByName(String name) {
	String sql = "SELECT * FROM roles WHERE name = ?";
	Role role = null;

	try (Connection connection = DatabaseConnection.getInstance().getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql)) {
	    pstmt.setString(1, name);
	    try (ResultSet rs = pstmt.executeQuery()) {
		if (rs.next()) {
		    role = mapResultSetToRole(rs);
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return role;
    }

    private Role mapResultSetToRole(ResultSet rs) throws SQLException {
	Role role = new Role(rs.getInt("id"), rs.getString("role_name"));
	return role;
    }
}
