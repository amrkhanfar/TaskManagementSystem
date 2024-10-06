<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home</title>
</head>
<body>

<c:if test="${not empty notification }"><p style="color=green;">${notification }</p></c:if>
<c:if test="${not empty errorMessage }"><p style="color=red;">${errorMessage }</p></c:if>

<h1>Welcome ${currentUser.name }!</h1>
<h2>Role: ${dataMapper.roleMap[currentUser.role_id] }</h2>
<br><br>

<c:if test="${dataMapper.roleMap[currentUser.role_id] == 'Manager' }">
	<a href="tasks" >My tasks</a><br>
	<a href="employees">View Employees</a><br>
	<a href="teams">View Teams</a><br>
	<a href="tasks?action=tasksManager">View All Tasks</a><br>
</c:if>

<c:if test="${dataMapper.roleMap[currentUser.role_id] == 'Team Leader' }">
	<a href="tasks" >My tasks</a><br>
	<a href="teams">View My Team</a><br>
	<a href="tasks?action=tasksTeamLeader">View All Tasks</a><br>
</c:if>

<c:if test="${dataMapper.roleMap[currentUser.role_id] == 'Developer' }">
	<a href="tasks" >My tasks</a><br>
</c:if>

</body>
</html> 