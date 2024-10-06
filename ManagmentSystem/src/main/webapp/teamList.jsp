<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Teams</title>
</head>
<body>

<c:if test="${not empty notification}">
    <p style="color:green;">${notification}</p>
</c:if>
<c:if test="${not empty errorMessage}">
    <p style="color:red;">${errorMessage}</p>
</c:if>

<h2>Teams</h2>
<br>
<a href="home.jsp">Home</a>
<c:if test="${dataMapper.roleMap[currentUser.role_id] == 'Manager'}">
    | <a href="teams?action=create">Create a team</a>
</c:if>

<c:forEach var="teamRepresentation" items="${teamRepresentations}">
    <h3>${teamRepresentation.data.team_name}</h3>
    <p>Members:</p>
    <ul>
        <c:forEach var="memberRepresentation" items="${teamRepresentation.members}">
            <li>
                ${memberRepresentation.data.name} - ${dataMapper.roleMap[memberRepresentation.data.role_id]}
                <c:if test="${not empty memberRepresentation.tasks}">
                    <ul>
                        <c:forEach var="task" items="${memberRepresentation.tasks}">
                            <li>${task.task_title} - ${dataMapper.statusMap[task.task_status]}</li>
                        </c:forEach>
                    </ul>
                </c:if>
            </li>
        </c:forEach>
    </ul>
    <c:if test="${dataMapper.roleMap[currentUser.role_id] == 'Manager'}">
        <a href="teams?action=edit&id=${teamRepresentation.data.id}">Edit</a>
    </c:if>
    <hr>
</c:forEach>

</body>
</html>