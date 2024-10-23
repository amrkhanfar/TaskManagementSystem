<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee Form</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>

<div class="container">
    <c:if test="${not empty notification }">
        <p class="notification">${notification}</p>
    </c:if>
    <c:if test="${not empty errorMessage }">
        <p class="error-message">${errorMessage}</p>
    </c:if>

    <h2>
        <c:choose>
            <c:when test="${action == 'add'}">Add New Employee</c:when>
            <c:when test="${action == 'edit'}">Edit Employee</c:when>
        </c:choose>
    </h2>

    <form action="${pageContext.request.contextPath}/employees" method="post" class="form">
        <input name="action" type="hidden" value="${action}"/>
        <input name="id" type="hidden" value="${employee.id}"/>

        <label>Name</label>
        <input name="name" type="text" value="${employee.name}" required class="input-field"/>
        <br>
        <label>Email</label>
        <input name="email" type="text" value="${employee.email}" required class="input-field"/>
        <br>
        <label>Password</label>
        <input name="password" type="password" required class="input-field"/>
        <br>
        <label>Role</label>
        <select name="role" class="select-field">
            <c:forEach var="role" items="${roles}">
                <option value="${role.id}" <c:if test="${role.id == employee.role_id}">selected</c:if> >${role.role_name}</option>
            </c:forEach>
        </select>
        <br>
        <label>Team</label>
        <select name="team" class="select-field">
            <c:forEach var="team" items="${teams}">
                <option value="${team.id}" <c:if test="${employee.team_id == team.id}">selected</c:if> >${team.team_name}</option>
            </c:forEach>
        </select>
        <br>
        <input type="submit" value="SUBMIT" class="button"/>
        <a href="${pageContext.request.contextPath}/employees" class="button cancel-button">CANCEL</a>
    </form>
</div>

</body>
</html>
