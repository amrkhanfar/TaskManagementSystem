<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Teams</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>

<div class="container">
    <c:if test="${not empty notification}">
        <p class="notification">${notification}</p>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <p class="error-message">${errorMessage}</p>
    </c:if>

    <h2>Teams</h2>
    <a href="${pageContext.request.contextPath}/home" class="button">Home</a>
    <c:if test="${dataMapper.roleMap[currentUser.role_id] == 'Manager'}">
        <a href="teams?action=create" class="button">Create a Team</a>
    </c:if>

    <c:forEach var="teamRepresentation" items="${teamRepresentations}">
        <div class="team">
            <h3>${teamRepresentation.data.team_name}</h3>
            <c:if test="${dataMapper.roleMap[currentUser.role_id] == 'Manager'}">
                <a href="teams?action=edit&id=${teamRepresentation.data.id}" class="button edit-button">Edit Team</a>
            </c:if>
            <table class="table">
                <thead>
                    <tr>
                        <th>Member Name</th>
                        <th>Role</th>
                        <th>Tasks</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="memberRepresentation" items="${teamRepresentation.members}">
                        <tr>
                            <td>${memberRepresentation.data.name}</td>
                            <td>${dataMapper.roleMap[memberRepresentation.data.role_id]}</td>
                            <td>
                                <c:if test="${not empty memberRepresentation.tasks}">
                                    <table class="nested-table">
                                        <thead>
                                            <tr>
                                                <th>Task Title</th>
                                                <th>Status</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="task" items="${memberRepresentation.tasks}">
                                                <tr data-task-id=${task.id }>
                                                    <td>${task.task_title}</td>
                                                    <td>${dataMapper.statusMap[task.task_status]}</td>
                                                    <td>
                                                        <a href="tasks?action=edit&id=${task.id}" class="edit-button-small">Edit</a> |
                                                        <a href="tasks?action=delete&id=${task.id}" class="delete-button-smal task-delete-button">Delete</a> |
                                                        
                                                        <c:choose>
                    										<c:when test="${dataMapper.statusMap[task.task_status] == 'pending' }">
                    											<a href="tasks?action=approve&id=${task.id}" class="submit-button-small approve-button">Approve</a>
                    										</c:when>
                    										<c:otherwise>
                    											<select class="select-field task-status-select-field" name="taskStatus">
                    												<c:forEach var="status" items="${dataMapper.statusMap}">
                    													<c:if test="${status.value != 'pending'}">
                    														<option value=${status.key } <c:if test="${task.task_status == status.key }"> selected </c:if> > ${status.value}</option>
                    													</c:if>
                    												</c:forEach>
                    											</select>
                    										</c:otherwise>
                  									    </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${dataMapper.roleMap[currentUser.role_id] == 'Manager'}">
                                    <a href="employees?action=edit&id=${memberRepresentation.data.id}" class="edit-button-small">Edit</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <hr>
        </div>
    </c:forEach>
</div>

<script>

$(document).ready(function() {
	
	$('.approve-button').on('click', function(event) {
		event.preventDefault();   // prevent default link behaviour
		
		var $link = $(this);
		var taskId = $link.closest('tr').data('task-id');
		
		$.ajax({
			url: 'tasks',
			type: 'POST',
			data: {
				action: 'approve',
				id: taskId
			},
			success: function(response){
				if (response.status === 'success') {
					var $taskRow = $link.closest('tr');
					$taskRow.remove();
					
					alert('Task has been approved successfuly');
				} else {
					alert('error: ' + response.message);
				}
			},
			error: function(error) {
				alert('An error occured' + error);
			}
		});
	});
	
	$('.task-status-select-field').on('change', function(event) {
		var $link = $(this);
		var taskId = $link.closest('tr').data('task-id');
		var newStatus = $link.val()
		
		$.ajax({
			url: 'tasks',
			type: 'POST',
			data: {
				action:'updateStatus',
				id: taskId,
				status: newStatus
			},
			success: function(response) {
				if(response.status === 'success') {
					
					alert(response.message);
				} else {
					alert('Error: ' + response.message )
				}
			},
			error: function(error) {
				alert('An error occurred' + error);
			}	
		});
	});
	
	$('.task-delete-button').on('click', function(event) {
		event.preventDefault();   // prevent default link behaviour
		
		var $link = $(this);
		var taskId = $link.closest('tr').data('task-id');
		
		$.ajax({
			url:' tasks',
			type: 'POST',
			data: {
				action: 'delete',
				id: taskId
			},
			success: function(response) {
				if(response.status === 'success') {
					$link.closest('tr').remove();
				} else {
					alert('ERROR: ' + response.message);
				}
			},
			error: function(error) {
				alert('An error occurred' + error);
			}
		});
		
	});
	
});
</script>

</body>
</html>