package com.managementsystem.controller;

import java.io.IOException;
import java.util.List;

import com.managementsystem.exception.AuthorizationException;
import com.managementsystem.model.Employee;
import com.managementsystem.model.Role;
import com.managementsystem.model.Team;
import com.managementsystem.service.EmployeeService;
import com.managementsystem.service.RoleService;
import com.managementsystem.service.TeamService;
import com.managementsystem.util.DataMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class EmployeeController extends HttpServlet {

	EmployeeService employeeService = new EmployeeService();
	RoleService roleService = new RoleService();
	TeamService teamService = new TeamService();
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		if(session == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		
		Employee currentUser = (Employee) session.getAttribute("currentUser");
		if(currentUser == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		
		if(Role.MANAGER != currentUser.getRole_id()) {
			request.setAttribute("errorMessage", "You don't have access.");
			request.getRequestDispatcher("home.jsp").forward(request, response);
		}
		
		
		String action = request.getParameter("action");
		
		if(action == null) {
			action = "listAllEmployees";
		}
		
		switch (action) {
		
		case "add":
			showAddForm(request, response);
			break;
		case "edit":
			showEditForm(request, response);
			break;
		case "delete":
			deleteEmployee(request, response, currentUser);
			break;
		default:
			listEmployees(request, response);
			break;
		}
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		if(session == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		
		Employee currentUser = (Employee)session.getAttribute("currentUser");
		if(currentUser == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		
		if(Role.MANAGER != currentUser.getRole_id()) {
			request.setAttribute("errorMessage", "You don't have access.");
			request.getRequestDispatcher("home.jsp").forward(request, response);
		}
		
		String action = request.getParameter("action");
		switch(action) {
		
		case "add":
			addEmployeeFromForm(request, response, currentUser);
			break;
		case "edit":
			editEmployeeFromForm(request, response, currentUser);
			break;
		default:
			listEmployees(request, response);
			break;
		}
	}
	
	private void listEmployees(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		List<Employee> employees = employeeService.getAllEmployees();
		request.setAttribute("employeeList", employees);
		//request.setAttribute("dataMapper", dataMapper);
		request.getRequestDispatcher("employeeList.jsp").forward(request, response);
		return;
	}
	
	private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setAttribute("action", "add");
		
		List<Role> roles = roleService.getAllRoles();
		List<Team> teams = teamService.getAllTeams();
		System.out.print(roles);
		request.setAttribute("roles", roles);
		request.setAttribute("teams", teams);
		
		request.getRequestDispatcher("employeeForm.jsp").forward(request, response);
		return;
	}
	
	private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		Employee employee = employeeService.getEmployeeById(Integer.parseInt(request.getParameter("id")));
		List<Role> roles = roleService.getAllRoles();
		List<Team> teams = teamService.getAllTeams();
		
		request.setAttribute("action", "edit");
		request.setAttribute("employee", employee);
		request.setAttribute("roles", roles);
		request.setAttribute("teams", teams);
		
		request.getRequestDispatcher("employeeForm.jsp").forward(request, response);
		return;
    }
	
	private void deleteEmployee(HttpServletRequest request, HttpServletResponse response, Employee currentUser) throws ServletException, IOException{
		int id = Integer.parseInt(request.getParameter("id"));
		
		try {
			boolean isDeleted = employeeService.deleteEmployee(id, currentUser.getId());
			if(isDeleted) {
				request.setAttribute("notification", "Employee deleted successfulyy");
			} else {
				request.setAttribute("errorMessage", "An error occured. Employee is not deleted.");
			}
			
			listEmployees(request, response);
			
		} catch (IllegalArgumentException | AuthorizationException e ) {
			request.setAttribute("errorMessage", e);
			listEmployees(request, response);
		}
		}
	
	private void addEmployeeFromForm(HttpServletRequest request, HttpServletResponse response, Employee currentUser) throws ServletException, IOException {
	
		try {
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String role = request.getParameter("role");
			String team = request.getParameter("team");
			
			if (name == null || email == null || password == null || role == null || team == null ||
		           name.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty() || team.isEmpty()) {
		            throw new IllegalArgumentException("Make sure no fields empty");
		        }
			
	        int roleIdParsed = Integer.parseInt(role);
	        int teamIdParsed = Integer.parseInt(team);
			Employee employee = new Employee (-1, name, email, roleIdParsed, teamIdParsed, password);
			
			employeeService.addEmployee(employee, currentUser.getId());
			
			request.setAttribute("notification", "New employee has been added successfuly");
			listEmployees(request, response);
		} catch (AuthorizationException | IllegalArgumentException  e) {
			// TODO Auto-generated catch block
			request.setAttribute("errorMessage", e.getMessage());
			showAddForm(request, response);
		}
	}
	

	private void editEmployeeFromForm(HttpServletRequest request, HttpServletResponse response, Employee currentUser) throws ServletException, IOException {
	    try {
	        String id = request.getParameter("id");
	        String name = request.getParameter("name");
	        String email = request.getParameter("email");
	        String password = request.getParameter("password");
	        String role = request.getParameter("role");
	        String team = request.getParameter("team");

	        if (id == null || name == null || email == null || password == null || role == null || team == null ||
	            id.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty() || team.isEmpty()) {
	            throw new IllegalArgumentException("Make sure no fields empty");
	        }


	        int idParsed = Integer.parseInt(id);
	        int roleIdParsed = Integer.parseInt(role);
	        int teamIdParsed = Integer.parseInt(team);
	        Employee employee = new Employee(idParsed, name, email, roleIdParsed, teamIdParsed, password);
	        
	        employeeService.updateEmployee(employee, currentUser.getId());

	        request.setAttribute("notification", "Employee has been updated successfully");
	        listEmployees(request, response);
	        
	    } catch (AuthorizationException | IllegalArgumentException e) {
	        request.setAttribute("errorMessage", e.getMessage());
	        showEditForm(request, response);
	    }
	}
	}
