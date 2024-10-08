package com.managementsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.managementsystem.model.Employee;
import com.managementsystem.model.Task;
import com.managementsystem.model.Team;
import com.managementsystem.util.DatabaseConnection;

public class TeamDAO implements TeamDAOInterface {

    @Override
    public int addTeam(Team team) {
	String sql = "INSERT INTO teams (team_name, team_leader_id) " + "VALUES (?, ?)";
	int generatedID = 0;

	try (Connection connection = DatabaseConnection.getInstance().getConnection();
		PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	    ps.setString(1, team.getTeam_name());
	    ps.setInt(2, team.getTeam_leader_id());

	    int affectedRows = ps.executeUpdate();
	    if (affectedRows == 0) {
		throw new SQLException("Creating team failed, no rows affected.");
	    }

	    try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
		if (generatedKeys.next()) {
		    generatedID = generatedKeys.getInt(1);
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return generatedID;
    }

    @Override
    public Team getTeamById(int id) {
	String sql = "SELECT * FROM teams t Where t.id = ?";
	Team foundTeam = null;
	try (Connection connection = DatabaseConnection.getInstance().getConnection();
		PreparedStatement ps = connection.prepareStatement(sql)) {

	    ps.setInt(1, id);
	    try (ResultSet rs = ps.executeQuery();) {
		if (rs.next()) {
		    foundTeam = mapResultSetToTeam(rs);
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return foundTeam;
    }

    @Override
    public Team getTeamByTeamLeaderId(int id) {
	String sql = "SELECT * FROM teams t Where t.team_leader_id = ?";
	Team foundTeam = null;
	try (Connection connection = DatabaseConnection.getInstance().getConnection();
		PreparedStatement ps = connection.prepareStatement(sql)) {

	    ps.setInt(1, id);
	    try (ResultSet rs = ps.executeQuery();) {
		if (rs.next()) {
		    foundTeam = mapResultSetToTeam(rs);
		}
	    }

	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return foundTeam;
    }

    @Override
    public List<Team> getAllTeams() {
	String sql = "SELECT * FROM teams";
	List<Team> teams = new ArrayList<>();

	try (Connection connection = DatabaseConnection.getInstance().getConnection();
		PreparedStatement ps = connection.prepareStatement(sql)) {

	    try (ResultSet rs = ps.executeQuery()) {
		while (rs.next()) {
		    Team team = mapResultSetToTeam(rs);
		    teams.add(team);
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return teams;
    }

    @Override
    public boolean updateTeam(Team team) {
	String sql = "UPDATE teams SET team_name = ?, team_leader_id = ? WHERE id = ?";
	boolean isUpdated = false;

	try (Connection connection = DatabaseConnection.getInstance().getConnection();
		PreparedStatement ps = connection.prepareStatement(sql)) {

	    ps.setString(1, team.getTeam_name());
	    ps.setInt(2, team.getTeam_leader_id());
	    ps.setInt(3, team.getId());

	    int affectedRows = ps.executeUpdate();
	    isUpdated = affectedRows > 0;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return isUpdated;
    }

    @Override
    public boolean deleteTeamById(int id) {
	String sql = "DELETE FROM teams WHERE id = ?";
	boolean isDeleted = false;

	try (Connection connection = DatabaseConnection.getInstance().getConnection();
		PreparedStatement ps = connection.prepareStatement(sql)) {

	    ps.setInt(1, id);
	    int affectedRows = ps.executeUpdate();
	    isDeleted = affectedRows > 0;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return isDeleted;
    }

    public static Team mapResultSetToTeam(ResultSet rs) throws SQLException {
	Team team = new Team(rs.getInt("id"), rs.getString("team_name"), rs.getInt("team_leader_id"));
	return team;
    }
}
