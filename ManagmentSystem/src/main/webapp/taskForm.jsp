<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Task Form</title>
</head>
<body>

<c:if test="${not empty notification }"><p style="color=green;">${notification }</p></c:if>
<c:if test="${not empty errorMessage }"><p style="color=red;">${errorMessage }</p></c:if>

<h2><c:choose>
        <c:when test="${action == 'create' || action == 'createAssignCurrent'}">Create New Task</c:when>
        <c:when test="${action == 'update'}">Edit Task</c:when>
    </c:choose>
</h2>

<form action="tasks" method="post">

	<input name="action" type="hidden" value="${action }">
	<input name="id" type="hidden" value="${task.id }">
	
	<input name="taskStatus" type="hidden" value="<c:choose>
    <c:when test="${dataMapper.roleMap[currentUser.role_id] == 'Developer'}">pending</c:when>
    <c:otherwise>assigned</c:otherwise>
</c:choose>">
	
	<label>Title</label>
	<input name="taskTitle" type="text" value="${task.task_title}">
	<br>
	<label>Description</label>
	<textarea name="taskDescription">${task.task_description}</textarea>
	<br>
	
	<label>Assigned to </label>
	 <c:if test="${dataMapper.roleMap[currentUser.role_id] == 'Developer'}"> 
	 	<input type="text" value="${currentUser.name }" readonly>
	 </c:if>
	 	 
	 <c:if test="${dataMapper.roleMap[currentUser.role_id] != 'Developer'}">
	 	<select name="assignedEmployee">
	 		<c:forEach var="employeeToDisplay" items="${employees }">
	 			<option value="${employeeToDisplay.id }" <c:if test="${action == 'createAssignCurrent' && employeeToDisplay.id == currentUser.id }"> selected </c:if> > ${employeeToDisplay.name} | Team: ${dataMapper.roleMap[employeeToDisplay.role_id]} | ${dataMapper.teamMap[employeeToDisplay.team_id]} </option>
	 		</c:forEach>
	 	</select>
	 </c:if>
    <br>
    <input type="submit" value="SUBMIT" />
	<a href="tasks">  CANCEL</a>
</form>

</body>
</html>