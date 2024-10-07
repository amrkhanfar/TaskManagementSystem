package com.managementsystem.dao;

import java.util.List;

import com.managementsystem.model.Team;

/**
 * Interface defining data access methods for Team entities.
 * 
 * @author Amr
 * @version Oct 7, 2024
 */
public interface TeamDAOInterface {

    /**
     * Adds a new Team to the database.
     * Note that the ID attribute will be discarded since 
     * the database will generate a unique one.
     *
     * @param team the Team object to be added.
     * @return the generated ID of the newly added team.
     */
    int addTeam(Team team);

    /**
     * Retrieves a Team by its ID.
     *
     * @param id the ID of the team
     * @return the Team object with the specified ID, Null if not found.
     */
    Team getTeamById(int id);

    /**
     * Retrieves a Team by the ID of its team leader.
     *
     * @param id the ID of the team leader.
     * @return the Team object led by the specified leader, or Null
     */
    Team getTeamByTeamLeaderId(int id);

    /**
     * Retrieves a list of all Teams.
     *
     * @return a list of all Teams.
     */
    List<Team> getAllTeams();

    /**
     * Updates the information of an existing Team.
     * The method will update the team based on the ID attribute in Team object.
     *
     * @param team the Team object with updated information
     * @return {@code true} if the update was successful, {@code false} otherwise
     */
    boolean updateTeam(Team team);

    /**
     * Deletes a Team by its unique ID.
     *
     * @param id the ID of the team to delete.
     * @return true if the deletion was successful (rows get affected), false otherwise.
     */
    boolean deleteTeamById(int id);
}
