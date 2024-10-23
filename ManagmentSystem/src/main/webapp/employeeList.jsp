<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee List</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>

<div class="container">
    <c:if test="${not empty notification}">
        <p class="notification">${notification}</p>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <p class="error-message">${errorMessage}</p>
    </c:if>

    <h1>Employee List</h1>
    <a href="${pageContext.request.contextPath}/home" class="button">Home</a>
    <a href="employees?action=add" class="button">Add New Employee</a>

    <table class="table">
        <thead>
            <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>Team</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="employee" items="${employeeList}">
                <tr>
                    <td>${employee.name}</td>
                    <td>${employee.email}</td>
                    <td>${dataMapper.roleMap[employee.role_id]}</td>
                    <td>${dataMapper.teamMap[employee.team_id]}</td>
                    <td>
                        <a href="employees?action=edit&id=${employee.id}" class="edit-button-small">Edit</a>
                        <a href="employees?action=delete&id=${employee.id}" class="delete-button-small">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

</body>
</html>
