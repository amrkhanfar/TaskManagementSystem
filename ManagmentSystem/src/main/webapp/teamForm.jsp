<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Team Form</title>
<link rel="stylesheet" href="styles.css">
</head>
<body>

<div class="container">
    <c:if test="${not empty errorMessage}">
        <p class="error-message">${errorMessage}</p>
    </c:if>

    <form action="teams" method="post" class="form">
        <input type="hidden" name="action" value="${action}"/>
        <c:if test="${team != null}">
            <input type="hidden" name="teamId" value="${team.id}"/>
        </c:if>

        <label for="teamName" class="label">Team Name:</label>
        <input type="text" name="teamName" id="teamName" value="${team.team_name}" class="input-field"/>

        <label class="label">Members:</label>
        <c:forEach var="employee" items="${allEmployees}">
            <input type="checkbox" name="members" value="${employee.id}" 
            <c:if test="${fn:contains(membersIds, employee.id)}">checked</c:if>
            /> ${employee.name} - ${dataMapper.roleMap[employee.role_id]}
        </c:forEach>

        <input type="submit" value="Submit" class="button"/>
    </form>
</div>

</body>
</html>
