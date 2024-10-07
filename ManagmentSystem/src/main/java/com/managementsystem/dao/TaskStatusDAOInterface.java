package com.managementsystem.dao;

import java.util.List;

import com.managementsystem.model.TaskStatus;

/**
 * 
 * TaskStatusDAOInterface - Interface defining data access methods for Task Status entities.
 * I made this class mainly for DataMapper in utilities because the stasuses are 
 * mapped in TaskStatus.java as public static fields.
 *
 * @author Amr
 * @version Oct 7, 2024
 */
public interface TaskStatusDAOInterface {
    
    /**
     * Note that all statuses are mapped in TaskStatus.java class as public static fields
     * You might need to modify them based on the database.
     */
    
    /**
     * Retrieves a list of all TaskStatuses.
     *
     * @return a list of all TaskStatuses
     */
    List<TaskStatus> getAllStatuses();
}
