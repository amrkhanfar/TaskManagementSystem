package com.managementsystem.util;

import java.util.List;
import com.managementsystem.model.Team;

/**
 * 
* TeamRepresentation - This class encapsulates teams with their members
* it was mainly created for handling forwarding data to jsps 
*
* @author 
* @version Oct 8, 2024
 */
public class TeamRepresentation {
    private Team data;
    private List<EmployeeRepresentation> members;

    public TeamRepresentation(Team data, List<EmployeeRepresentation> members) {
        this.data = data;
        this.members = members;
    }

    public Team getData() {
        return data;
    }

    public void setData(Team data) {
        this.data = data;
    }

    public List<EmployeeRepresentation> getMembers() {
        return members;
    }

    public void setMembers(List<EmployeeRepresentation> members) {
        this.members = members;
    }
}