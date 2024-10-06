package com.managementsystem.service;

import java.util.List;

import com.managementsystem.dao.RoleDAO;
import com.managementsystem.model.Role;

public class RoleService {
	
	private RoleDAO roleDAO = new RoleDAO();
	
	public List<Role> getAllRoles(){
		return roleDAO.getAllRoles();
	}
	
	public Role getRoleById(int id) {
		return roleDAO.getRoleById(id);
	}
	
	public Role getRoleByName(String name) {
		return roleDAO.getRoleByName(name);
	}
}
