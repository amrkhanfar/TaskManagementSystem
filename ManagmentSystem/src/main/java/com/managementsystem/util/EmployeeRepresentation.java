package com.managementsystem.util;

import java.util.List;
import com.managementsystem.model.Employee;
import com.managementsystem.model.Task;

public class EmployeeRepresentation {
    private Employee data;
    private List<Task> tasks;

    public EmployeeRepresentation(Employee data, List<Task> tasks) {
        this.data = data;
        this.tasks = tasks;
    }

    public Employee getData() {
        return data;
    }

    public void setData(Employee data) {
        this.data = data;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}