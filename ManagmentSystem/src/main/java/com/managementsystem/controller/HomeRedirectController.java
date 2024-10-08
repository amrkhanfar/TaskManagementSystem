package com.managementsystem.controller;

import java.io.IOException;

import com.managementsystem.model.Employee;
import com.managementsystem.util.DataMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 
* HomeRedirectController - Controller responsible for handling redirection to the home page.
*
* @author Amr
* @version Oct 8, 2024
 */
public class HomeRedirectController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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

	request.setAttribute("currentUser", currentUser);
	request.getRequestDispatcher("home.jsp").forward(request, response);
	return;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	doGet(request, response);
    }
}
