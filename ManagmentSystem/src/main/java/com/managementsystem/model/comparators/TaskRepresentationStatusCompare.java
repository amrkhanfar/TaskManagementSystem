package com.managementsystem.model.comparators;

import java.util.Comparator;

import com.managementsystem.util.TaskRepresntation;

public class TaskRepresentationStatusCompare implements Comparator<TaskRepresntation> {
    
    @Override
    public int compare(TaskRepresntation tr1, TaskRepresntation tr2) {
	
	if(tr1.getData().getTask_status() > tr2.getData().getTask_status()) {
	    return 1;
	} else if (tr1.getData().getTask_status() < tr2.getData().getTask_status()) {
	    return -1;
	} else {
	    return 0;
	}
    }
}
