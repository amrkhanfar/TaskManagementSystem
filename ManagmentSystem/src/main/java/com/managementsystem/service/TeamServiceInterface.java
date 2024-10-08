package com.managementsystem.service;

import java.util.List;
import com.managementsystem.exception.AuthorizationException;
import com.managementsystem.model.Team;

/**
 * Interface defining the service methods for Team operations.
 * 
 * @author Amr
 */
public interface TeamServiceInterface {

    /**
     * Creates a new team.
     *
     * @param team      the Team object to create
     * @param creatorId the ID of the user creating the team
     * @return the generated ID of the new team
     * @throws AuthorizationException if the creator is not authorized
     */
    int createTeam(Team team, int creatorId) throws AuthorizationException;

    /**
     * Retrieves a team by its ID.
     *
     * @param teamId the ID of the team to retrieve
     * @return the Team object, or null if not found
     */
    Team getTeamById(int teamId);

    /**
     * Retrieves all teams.
     *
     * @return a list of Team objects
     */
    List<Team> getAllTeams();

    /**
     * Updates a team's information.
     *
     * @param team      the Team object with updated information
     * @param updaterId the ID of the user performing the update
     * @return true if the update was successful, false otherwise
     * @throws AuthorizationException if the updater is not authorized
     */
    boolean updateTeam(Team team, int updaterId) throws AuthorizationException;

    /**
     * Retrieves a team by the ID of its team leader.
     *
     * @param id the ID of the team leader
     * @return the Team object led by the specified leader, or null if not found
     */
    Team getTeamByTeamLeaderId(int id);

    /**
     * Deletes a team by its ID.
     *
     * @param teamId    the ID of the team to delete
     * @param deleterId the ID of the user performing the deletion
     * @return true if the deletion was successful, false otherwise
     * @throws AuthorizationException if the deleter is not authorized
     */
    boolean deleteTeam(int teamId, int deleterId) throws AuthorizationException;
}