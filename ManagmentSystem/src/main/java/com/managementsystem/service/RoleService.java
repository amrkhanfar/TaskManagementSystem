package com.managementsystem.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.managementsystem.dao.RoleDAO;
import com.managementsystem.model.Role;

public class RoleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleService.class);
    private RoleDAO roleDAO = new RoleDAO();

    public List<Role> getAllRoles() {
	LOGGER.debug("Entering getAllRoles");
	
	List<Role> allRoles = roleDAO.getAllRoles();
	
	LOGGER.debug("Exiting getAllRoles");
	return allRoles;
    }

    public Role getRoleById(int id) {
	LOGGER.debug("Entering getRoleById with role id: {}", id);
	
	Role role = roleDAO.getRoleById(id);
	
	LOGGER.debug("Exiting getRoleById with role: {}", role);
	return role;
    }

    public Role getRoleByName(String name) {
	LOGGER.debug("Entring getRoleByName with role name: {}", name);
	
	Role role = roleDAO.getRoleByName(name);
	
	LOGGER.debug("Exiting getRoleByName with role: {}", role);
	return role;
    }
}
