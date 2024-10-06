<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Team Form</title>
</head>
<body>

<c:if test="${not empty errorMessage}">
    <p style="color:red;">${errorMessage}</p>
</c:if>

<form action="teams" method="post">
    <input type="hidden" name="action" value="${action}"/>
    <c:if test="${team != null}">
        <input type="hidden" name="teamId" value="${team.id}"/>
    </c:if>
    <label for="teamName">Team Name:</label>
    <input type="text" name="teamName" id="teamName" value="${team != null ? team.team_name : ''}"/>
    <br><br>

    <label>Members:</label>
    <br>
    <c:forEach var="employee" items="${eligibleEmployees}">
        <input type="checkbox" name="members" value="${employee.id}"
            <c:if test="${members != null && fn:contains(members, employee)}">
                checked="checked"
            </c:if>
        /> ${employee.name } - ${dataMapper.roleMap[employee.role_id]}
        <br>
    </c:forEach>
    <br>
    <input type="submit" value="Submit"/>
</form>

</body>
</html>