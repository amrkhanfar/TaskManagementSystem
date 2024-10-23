package com.managementsystem.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.managementsystem.dao.RoleDAO;
import com.managementsystem.dao.TaskStatusDAO;
import com.managementsystem.dao.TeamDAO;
import com.managementsystem.model.Role;
import com.managementsystem.model.TaskStatus;
import com.managementsystem.model.Team;

/**
 * 
* DataMapper - Class for mapping ids to their names.
* This class was created mainly for handling data forwarding to JSPs.
*
* @author Amr
* @version Oct 8, 2024
 */
public class DataMapper {
    /**
     * This class is used only in the authentication servlet as it cashes
     * every map in the object and stores it as a session attribute.
     * 
     * It might need some refactoring in the future maybe access it in a singleton
     * and gets updated whenver a new record is added to reams/roles/taskstatuses.
     */
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DataMapper.class);
    
    private RoleDAO roleDAO;
    private TeamDAO teamDAO;
    private TaskStatusDAO taskStatusDAO;

    private Map<Integer, String> roleMap;
    private Map<Integer, String> teamMap;
    private Map<Integer, String> statusMap;
    

    public DataMapper() {
	LOGGER.debug("Entriing DataMapper constructor");
	roleDAO = new RoleDAO();
	teamDAO = new TeamDAO();
	taskStatusDAO = new TaskStatusDAO();

	roleMap = new HashMap<>();
	for (Role role : roleDAO.getAllRoles()) {
	    roleMap.put(role.getId(), role.getRole_name());
	}

	teamMap = new HashMap<>();
	for (Team team : teamDAO.getAllTeams()) {
	    teamMap.put(team.getId(), team.getTeam_name());
	}

	statusMap = new HashMap<>();
	for (TaskStatus status : taskStatusDAO.getAllStatuses()) {
	    statusMap.put(status.getId(), status.getStatus_name().trim());
	}
	LOGGER.debug("Exiting DataMapper constructor");
	LOGGER.info("Roles/Teams/TaskStatuses has been mapped succefully.");
    }

    public Map<Integer, String> getRoleMap() {
	return this.roleMap;
    }

    public Map<Integer, String> getTeamMap() {
	return this.teamMap;
    }

    public Map<Integer, String> getStatusMap() {
	return this.statusMap;
    }
}
