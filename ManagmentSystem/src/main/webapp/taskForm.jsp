<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Task Form</title>
<link rel="stylesheet" href="styles.css">
</head>
<body>

<div class="container">
    <c:if test="${not empty notification}">
        <p class="notification">${notification}</p>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <p class="error-message">${errorMessage}</p>
    </c:if>

    <h2><c:choose>
        <c:when test="${action == 'create' || action == 'createAssignCurrent'}">Create New Task</c:when>
        <c:when test="${action == 'update'}">Edit Task</c:when>
    </c:choose></h2>

    <form action="tasks" method="post" class="form">
        <input name="action" type="hidden" value="${action}">
        <input name="id" type="hidden" value="${task.id}">
        <input name="taskStatus" type="hidden" value="<c:choose>
            <c:when test="${dataMapper.roleMap[currentUser.role_id] == 'Developer'}">pending</c:when>
            <c:otherwise>assigned</c:otherwise>
        </c:choose>">

        <label class="label">Title</label>
        <input class="input-field" name="taskTitle" type="text" value="${task.task_title}">

        <label class="label">Description</label>
        <textarea class="input-field" name="taskDescription">${task.task_description}</textarea>

        <label class="label">Assigned to</label>
        <c:if test="${dataMapper.roleMap[currentUser.role_id] == 'Developer'}"> 
            <input class="input-field" type="text" value="${currentUser.name}" readonly>
        </c:if>

        <c:if test="${dataMapper.roleMap[currentUser.role_id] != 'Developer'}">
            <select class="select-field" name="assignedEmployee">
                <c:forEach var="employeeToDisplay" items="${employees}">
                    <option value="${employeeToDisplay.id}" 
                        <c:if test="${action == 'edit' && employeeToDisplay.id == assignedEmployee.id}"> selected </c:if>>
                        ${employeeToDisplay.name} | Team: ${dataMapper.roleMap[employeeToDisplay.role_id]} | ${dataMapper.teamMap[employeeToDisplay.team_id]}
                    </option>
                </c:forEach>
            </select>
        </c:if>

        <div class="button-group">
            <input class="button" type="submit" value="SUBMIT" />
            <a href="tasks" class="button cancel-button">CANCEL</a>
        </div>
    </form>
</div>

</body>
</html>
