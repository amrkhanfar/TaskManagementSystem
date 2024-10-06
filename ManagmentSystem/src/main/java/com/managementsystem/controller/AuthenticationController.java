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

public class AuthenticationController extends HttpServlet {
	EmployeeService employeeService = new EmployeeService();
	DataMapper dataMapper = new DataMapper();
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
		try {
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			if(email == null || password == null || email.isEmpty() || password.isEmpty()) {
				throw new IllegalArgumentException("Make sure no fields empty");
			}
			
			
			Employee employee = employeeService.authenticate(email, password);
			HttpSession session = request.getSession();
			session.setAttribute("currentUser", employee);
			session.setAttribute("dataMapper", dataMapper);
			response.sendRedirect("home.jsp");
			
		} catch (AuthenticationException | IllegalArgumentException e) {
			System.out.print(e.getMessage());
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		request.getRequestDispatcher("login.jsp").forward(request, response);
		return;
	}
}
