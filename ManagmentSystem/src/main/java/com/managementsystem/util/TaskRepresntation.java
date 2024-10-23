package com.managementsystem.util;

import com.managementsystem.model.Employee;
import com.managementsystem.model.Task;

public class TaskRepresntation {
    private Task data;
    private Employee assignedEmployee;
    
    public TaskRepresntation(Task data, Employee assignedEmployee) {
	super();
	this.data = data;
	this.assignedEmployee = assignedEmployee;
    }
    public Task getData() {
        return data;
    }
    public void setData(Task data) {
        this.data = data;
    }
    public Employee getAssignedEmployee() {
        return assignedEmployee;
    }
    public void setAssignedEmployee(Employee assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
    }
    
    
}
