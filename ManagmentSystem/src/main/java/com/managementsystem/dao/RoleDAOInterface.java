package com.managementsystem.dao;

import java.util.List;

import com.managementsystem.model.Role;
import com.managementsystem.model.Task;

/**
 * 
 * RoleDAOInterface - Interface defining data access methods for Role entities.
 * I made this class mainly for DataMapper in utilities because the roles are 
 * mapped in Roles.java as public static fields.
 *
 * @author Amr
 * @version Oct 7, 2024
 */
public interface RoleDAOInterface {
    /**
     * Note that all roles are mapped in Role.java class ass public static fields
     * You might need to modify them based on the database.
     */
    
    /**
     * Retrieves a list of all Roles.
     *
     * @return a list of all Roles
     */
    List<Role> getAllRoles();

    /**
     * Retrieves a Role by its unique ID.
     *
     * @param id the ID of the role
     * @return the Role object with the specified ID. Null if not found
     */
    Role getRoleById(int id);

    /**
     * Retrieves a Role by its name.
     *
     * @param name the name of the role
     * @return the Role object with the specified name, or null if not found
     */
    Role getRoleByName(String name);
}
