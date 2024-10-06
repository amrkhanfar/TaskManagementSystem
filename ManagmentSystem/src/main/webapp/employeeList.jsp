<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Employee List</title>
</head>
<body>

<c:if test="${not empty notification }"><p style="color=green;">${notification }</p></c:if>
<c:if test="${not empty errorMessage }"><p style="color=red;">${errorMessage }</p></c:if>

<h1>Employee List</h1> <br>
<a href="home.jsp">Home</a> | <a href="employees?action=add">Add new employee</a>
<br>
<table>
	<tr>
		<th>Name</th>
		<th>Email</th>
		<th>Role</th>
		<th>Team</th>
		<th>Actions</th>
	</tr>

	<c:forEach var="employee" items="${employeeList}">
		<tr>
			<td>${employee.name }</td>
			<td>${employee.email }</td>
			<td>${dataMapper.roleMap[employee.role_id] }</td>
			<td>${dataMapper.teamMap[employee.team_id]}</td>
			<td>
				<a href="employees?action=edit&id=${employee.id }">Edit</a>
				<a href="employees?action=delete&id=${employee.id}" >Delete</a>
			</td>
		</tr>
	</c:forEach>

</table>

</body>
</html>