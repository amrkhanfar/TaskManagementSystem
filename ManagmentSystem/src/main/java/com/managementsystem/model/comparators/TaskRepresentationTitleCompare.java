package com.managementsystem.model.comparators;

import java.util.Comparator;

import com.managementsystem.util.TaskRepresntation;

public class TaskRepresentationTitleCompare implements Comparator<TaskRepresntation> {
    
    @Override
    public int compare(TaskRepresntation tr1, TaskRepresntation tr2) {
        return tr1.getData().getTask_title().compareToIgnoreCase(tr2.getData().getTask_title());
    }
}
