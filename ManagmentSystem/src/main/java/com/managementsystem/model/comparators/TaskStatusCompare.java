package com.managementsystem.model.comparators;

import java.util.Comparator;

import com.managementsystem.model.Task;

public class TaskStatusCompare implements Comparator<Task> {

    @Override
    public int compare (Task t1, Task t2) {
	if(t1.getTask_status() > t2.getTask_status()) {
	    return 1;
	} else if (t1.getTask_status() < t2.getTask_status()) {
	    return -1;
	} else {
	    return 0;
	}
    }
}
