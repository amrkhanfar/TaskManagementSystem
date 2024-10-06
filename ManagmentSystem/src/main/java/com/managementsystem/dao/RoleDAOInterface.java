package com.managementsystem.dao;

import java.util.List;

import com.managementsystem.model.Role;
import com.managementsystem.model.Task;

public interface RoleDAOInterface {
	List<Role> getAllRoles();
	Role getRoleById(int id);
	Role getRoleByName(String name);
}
