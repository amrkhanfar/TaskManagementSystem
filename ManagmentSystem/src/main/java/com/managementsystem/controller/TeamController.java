package com.managementsystem.controller;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.managementsystem.exception.AuthorizationException;
import com.managementsystem.model.Employee;
import com.managementsystem.model.Role;
import com.managementsystem.model.Task;
import com.managementsystem.model.Team;
import com.managementsystem.service.EmployeeService;
import com.managementsystem.service.TaskService;
import com.managementsystem.service.TeamService;
import com.managementsystem.util.EmployeeRepresentation;
import com.managementsystem.util.TeamRepresentation;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class TeamController extends HttpServlet {

    private TeamService teamService = new TeamService();
    private EmployeeService employeeService = new EmployeeService();
    private TaskService taskService = new TaskService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Employee currentUser = (Employee) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        if(currentUser.getRole_id() == Role.DEVELOPER) {
        	response.sendRedirect("home.jsp");
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "listTeams";
        }

        switch (action) {

            case "create":
                showCreateTeamForm(request, response, currentUser);
                break;
            case "edit":
                showEditTeamForm(request, response, currentUser);
                break;
            default:
                listTeams(request, response, currentUser);
                break;
        }

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Employee currentUser = (Employee) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        switch (action) {

            case "create":
                createTeam(request, response, currentUser);
                break;
            case "edit":
                editTeam(request, response, currentUser);
                break;
            default:
                listTeams(request, response, currentUser);
        }
    }

    private void listTeams(HttpServletRequest request, HttpServletResponse response, Employee currentUser)
            throws ServletException, IOException {

        List<TeamRepresentation> teamRepresentations = new ArrayList<>();

        if (currentUser.getRole_id() == Role.MANAGER) {
            List<Team> teams = teamService.getAllTeams();
            for (Team team : teams) {
                List<Employee> members = employeeService.getEmployeesByTeam(team.getId());
                List<EmployeeRepresentation> memberRepresentations = new ArrayList<>();
                for (Employee member : members) {
                    EmployeeRepresentation memberRepresentation = new EmployeeRepresentation(member, null);
                    memberRepresentations.add(memberRepresentation);
                }
                TeamRepresentation teamRepresentation = new TeamRepresentation(team, memberRepresentations);
                teamRepresentations.add(teamRepresentation);
            }
        } else if (currentUser.getRole_id() == Role.TEAM_LEADER) {
            Team team = teamService.getTeamById(currentUser.getTeam_id());
            if (team != null) {
                List<Employee> members = employeeService.getEmployeesByTeam(team.getId());
                List<EmployeeRepresentation> memberRepresentations = new ArrayList<>();
                for (Employee member : members) {
                    List<Task> tasks = taskService.getTasksByEmployee(member.getId());
                    EmployeeRepresentation memberRepresentation = new EmployeeRepresentation(member, tasks);
                    memberRepresentations.add(memberRepresentation);
                }
                TeamRepresentation teamRepresentation = new TeamRepresentation(team, memberRepresentations);
                teamRepresentations.add(teamRepresentation);
            }
        } else {
            request.setAttribute("errorMessage", "Access denied");
            request.getRequestDispatcher("home.jsp").forward(request, response);
            return;
        }

        request.setAttribute("teamRepresentations", teamRepresentations);
        request.getRequestDispatcher("teamList.jsp").forward(request, response);
    }

    private void showCreateTeamForm(HttpServletRequest request, HttpServletResponse response, Employee currentUser)
            throws ServletException, IOException {

        if (currentUser.getRole_id() != Role.MANAGER) {
            request.setAttribute("errorMessage", "Access denied");
            request.getRequestDispatcher("home.jsp").forward(request, response);
            return;
        }

        List<Employee> allEmployees = employeeService.getAllEmployees();
        request.setAttribute("eligibleEmployees", allEmployees);
        request.setAttribute("action", "create");

        request.getRequestDispatcher("teamForm.jsp").forward(request, response);
    }

    private void showEditTeamForm(HttpServletRequest request, HttpServletResponse response, Employee currentUser)
            throws ServletException, IOException {

        if (currentUser.getRole_id() != Role.MANAGER) {
            request.setAttribute("errorMessage", "Access denied");
            request.getRequestDispatcher("home.jsp").forward(request, response);
            return;
        }

        String id = request.getParameter("id");
        if (id == null) {
            request.setAttribute("errorMessage", "Team ID is required");
            listTeams(request, response, currentUser);
            return;
        }
        int teamId = Integer.parseInt(id);
        Team team = teamService.getTeamById(teamId);
        if (team == null) {
            request.setAttribute("errorMessage", "Team not found");
            listTeams(request, response, currentUser);
            return;
        }


        List<Employee> members = employeeService.getEmployeesByTeam(teamId);


        List<Employee> allEmployees = employeeService.getAllEmployees();
        request.setAttribute("eligibleEmployees", allEmployees);

        request.setAttribute("team", team);
        request.setAttribute("members", members);
        request.setAttribute("action", "edit");

        request.getRequestDispatcher("teamForm.jsp").forward(request, response);
    }

    private void createTeam(HttpServletRequest request, HttpServletResponse response, Employee currentUser)
            throws ServletException, IOException {
       
    }

    private void editTeam(HttpServletRequest request, HttpServletResponse response, Employee currentUser)
            throws ServletException, IOException {
       
    }
}