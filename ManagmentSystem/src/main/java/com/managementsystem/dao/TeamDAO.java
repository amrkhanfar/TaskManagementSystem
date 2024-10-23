package com.managementsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.managementsystem.model.Team;
import com.managementsystem.util.DatabaseConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeamDAO implements TeamDAOInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamDAO.class);

    @Override
    public int addTeam(Team team) {
        LOGGER.debug("Entering addTeam with team: {}", team.getTeam_name());

        String sql = "INSERT INTO teams (team_name, team_leader_id) VALUES (?, ?)";
        int generatedID = 0;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, team.getTeam_name());
            ps.setInt(2, team.getTeam_leader_id());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                LOGGER.error("Creating team failed, no rows affected.");
                throw new SQLException("Creating team failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedID = generatedKeys.getInt(1);
                    LOGGER.info("Team added successfully with ID: {}", generatedID);
                }
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in addTeam: {}", e.getMessage());
        }

        LOGGER.debug("Exiting addTeam with generatedID: {}", generatedID);
        return generatedID;
    }

    @Override
    public Team getTeamById(int id) {
        LOGGER.debug("Entering getTeamById with id: {}", id);

        String sql = "SELECT * FROM teams t WHERE t.id = ?";
        Team foundTeam = null;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    foundTeam = mapResultSetToTeam(rs);
                    LOGGER.debug("Team found: {}", foundTeam);
                } else {
                    LOGGER.info("No team found with id: {}", id);
                }
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in getTeamById: {}", e.getMessage());
        }

        LOGGER.debug("Exiting getTeamById");
        return foundTeam;
    }

    @Override
    public Team getTeamByTeamLeaderId(int id) {
        LOGGER.debug("Entering getTeamByTeamLeaderId with teamLeaderId: {}", id);

        String sql = "SELECT * FROM teams t WHERE t.team_leader_id = ?";
        Team foundTeam = null;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    foundTeam = mapResultSetToTeam(rs);
                    LOGGER.debug("Team found: {}", foundTeam);
                } else {
                    LOGGER.info("No team found with teamLeaderId: {}", id);
                }
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in getTeamByTeamLeaderId: {}", e.getMessage());
        }

        LOGGER.debug("Exiting getTeamByTeamLeaderId");
        return foundTeam;
    }

    @Override
    public List<Team> getAllTeams() {
        LOGGER.debug("Entering getAllTeams");

        String sql = "SELECT * FROM teams";
        List<Team> teams = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Team team = mapResultSetToTeam(rs);
                    teams.add(team);
                }
                LOGGER.debug("Retrieved {} teams", teams.size());
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in getAllTeams: {}", e.getMessage());
        }

        LOGGER.debug("Exiting getAllTeams");
        return teams;
    }

    @Override
    public boolean updateTeam(Team team) {
        LOGGER.debug("Entering updateTeam with team: {}", team);

        String sql = "UPDATE teams SET team_name = ?, team_leader_id = ? WHERE id = ?";
        boolean isUpdated = false;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, team.getTeam_name());
            ps.setInt(2, team.getTeam_leader_id());
            ps.setInt(3, team.getId());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                isUpdated = true;
                LOGGER.info("Team with id {} updated successfully.", team.getId());
            } else {
                LOGGER.error("No team found with id {}. Update not performed.", team.getId());
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in updateTeam: {}", e.getMessage());
        }

        LOGGER.debug("Exiting updateTeam with isUpdated: {}", isUpdated);
        return isUpdated;
    }

    @Override
    public boolean deleteTeamById(int id) {
        LOGGER.debug("Entering deleteTeamById with id: {}", id);

        String sql = "DELETE FROM teams WHERE id = ?";
        boolean isDeleted = false;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                isDeleted = true;
                LOGGER.info("Team with id {} deleted successfully.", id);
            } else {
                LOGGER.error("No team found with id {}. Deletion not performed.", id);
            }

        } catch (SQLException e) {
            LOGGER.error("SQLException in deleteTeamById: {}", e.getMessage(), e);
        }

        LOGGER.debug("Exiting deleteTeamById with isDeleted: {}", isDeleted);
        return isDeleted;
    }

    public static Team mapResultSetToTeam(ResultSet rs) throws SQLException {
        Team team = new Team(
                rs.getInt("id"),
                rs.getString("team_name"),
                rs.getInt("team_leader_id")
        );
        return team;
    }
}