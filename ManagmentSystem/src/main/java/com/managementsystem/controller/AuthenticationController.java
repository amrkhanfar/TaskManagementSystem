package com.managementsystem.controller;

import java.io.IOException;

import com.managementsystem.exception.AuthenticationException;
import com.managementsystem.model.Employee;
import com.managementsystem.service.EmployeeService;
import com.managementsystem.util.DataMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 
* AuthenticationController - Controller for handling authentication (login) requests.
*
* @author 
* @version Oct 8, 2024
 */
public class AuthenticationController extends HttpServlet {
    EmployeeService employeeService = new EmployeeService();
    
    /** Utility for mapping data such as IDs to names. */
    DataMapper dataMapper = new DataMapper();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	try {
	    String email = request.getParameter("email");
	    String password = request.getParameter("password");

	    if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
		throw new IllegalArgumentException("Make sure no fields empty");
	    }

	    Employee employee = employeeService.authenticate(email, password);
	    HttpSession session = request.getSession();
	    session.setAttribute("currentUser", employee);
	    
	    /**
	     * data mapper contains maps for mapping the
	     * id->team name/task status name/ role name 
	     * check the util sub package.
	     */
	    session.setAttribute("dataMapper", dataMapper);
	    request.getRequestDispatcher("home.jsp").forward(request, response);
	    return;
	} catch (AuthenticationException | IllegalArgumentException e) {
	    request.setAttribute("errorMessage", e.getMessage());
	    response.sendRedirect(request.getContextPath() + "/home");
	    return;
	}
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	request.getRequestDispatcher("login.jsp").forward(request, response);
	return;
    }
}
