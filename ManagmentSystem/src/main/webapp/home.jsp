<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    

<%
	if (session.getAttribute("currentUser") == null){
	    response.sendRedirect(request.getContextPath() + "/home");
	}
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>

<a href="logout" class="logout-button">Logout</a>

<div class="container">
    <c:if test="${not empty notification }">
        <p class="notification">${notification}</p>
    </c:if>
    <c:if test="${not empty errorMessage }">
        <p class="error-message">${errorMessage}</p>
    </c:if>

    <h1>Welcome, ${currentUser.name}</h1>
    <h2>Role: ${dataMapper.roleMap[currentUser.role_id]}</h2>

    <div class="links">
        <c:if test="${dataMapper.roleMap[currentUser.role_id] == 'Manager'}">
            <a href="tasks" class="button">Tasks</a>
            <a href="employees" class="button">View Employees</a>
            <a href="teams" class="button">View Teams</a>
        </c:if>

        <c:if test="${dataMapper.roleMap[currentUser.role_id] == 'Team Leader'}">
            <a href="tasks" class="button">Tasks</a>
            <a href="teams" class="button">My Team</a>
        </c:if>

        <c:if test="${dataMapper.roleMap[currentUser.role_id] == 'Developer'}">
            <a href="tasks" class="button">My Tasks</a>
        </c:if>
    </div>
</div>

</body>
</html>
