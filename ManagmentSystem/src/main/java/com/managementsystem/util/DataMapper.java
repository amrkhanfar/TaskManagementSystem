package com.managementsystem.util;


import java.util.HashMap;
import java.util.Map;

import com.managementsystem.dao.RoleDAO;
import com.managementsystem.dao.TaskStatusDAO;
import com.managementsystem.dao.TeamDAO;
import com.managementsystem.model.Role;
import com.managementsystem.model.TaskStatus;
import com.managementsystem.model.Team;

public class DataMapper {
	
	private RoleDAO roleDAO;
    private TeamDAO teamDAO;
    private TaskStatusDAO taskStatusDAO;
    
    private Map<Integer, String> roleMap;
    private Map<Integer, String> teamMap;
    private Map<Integer, String> statusMap;
    
    
    public DataMapper(){
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
    		statusMap.put(status.getId(), status.getStatus_name());
        }
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
