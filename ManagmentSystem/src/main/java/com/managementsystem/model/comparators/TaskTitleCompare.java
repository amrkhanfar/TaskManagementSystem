package com.managementsystem.model.comparators;

import java.util.Comparator;

import com.managementsystem.model.Task;

public class TaskTitleCompare implements Comparator<Task> {
    
    @Override
    public int compare(Task t1, Task t2) {
	return t1.getTask_title().compareToIgnoreCase(t2.getTask_title());
    }
}
