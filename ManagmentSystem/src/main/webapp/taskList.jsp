<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tasks</title>
</head>
<body>

<c:if test="${not empty notification }"><p style="color=green;">${notification }</p></c:if>
<c:if test="${not empty errorMessage }"><p style="color=red;">${errorMessage }</p></c:if>

<c:if test="${action == 'listMyTasks' }">
<h2>My tasks</h2>
<br>
<a href="home.jsp">Home</a> | <a href="tasks?action=createAssignCurrent"]>Create a task</a>
<table>
	<tr>
		<th>Title</th>
		<th>Status</th>
		<th>Description</th>
		<th>Action</th>
	</tr>
	
	<c:forEach var="task" items="${userTasks }">
		<tr>
			<td>${task.task_title }</td>
			<td>${dataMapper.statusMap[task.task_status] }</td>
			<td>${task.task_description }</td>
			<td><a href="tasks?action=edit&id=${task.id }">Edit</a></td>
		</tr>
	</c:forEach>

</table>
</c:if>

<c:if test="${dataMapper.roleMap[currentUser.role_id] != 'Developer'}">

<h2>Pending Tasks</h2>

<table>
	<tr>
		<th>Title</th>
		<th>Status</th>
		<th>Description</th>
		<th>Action</th>
	</tr>
	
	<c:forEach var="task" items="${pendingTasks }">
		<tr>
			<td>${task.task_title }</td>
			<td>${dataMapper.statusMap[task.task_status] }</td>
			<td>${task.task_description }</td>
			<td><a href="tasks?action=approve&id=${task.id }">Approve</a></td>
		</tr>
	</c:forEach>

</table>
</c:if>



</body>
</html>