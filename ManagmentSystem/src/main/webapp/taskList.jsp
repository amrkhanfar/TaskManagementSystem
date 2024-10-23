<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tasks</title>
<link rel="stylesheet" href="styles.css">
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

    <c:choose>
        <c:when test="${dataMapper.roleMap[currentUser.role_id] == 'Developer'}">
            <a href="home.jsp" class="button">Home</a> | <a href="tasks?action=createAssignCurrent" class="button">Create a Task</a>
        </c:when>
        <c:otherwise>
            <a href="${pageContext.request.contextPath}/home" class="button">Home</a> | <a href="tasks?action=create" class="button">Create a Task</a>
        </c:otherwise>
    </c:choose>

    <h2>My Tasks</h2>
    <table class="table">
        <tr>
            <th>Title</th>
            <th>Status</th>
            <th>Description</th>
            <th>Action</th>
        </tr>
        <c:forEach var="task" items="${userTasks}">
            <tr data-task-id="${task.id}">
                <td>${task.task_title}</td>
                <td>${dataMapper.statusMap[task.task_status]}</td>
                <td>${task.task_description}</td>
                <td>
                    <c:if test="${dataMapper.roleMap[currentUser.role_id] != 'Developer'}">
                    	<a href="tasks?action=edit&id=${task.id}" class="edit-button-small">Edit</a> | 
                        <a href="tasks?action=delete&id=${task.id}" class="delete-button-small task-delete-button">Delete</a> |
                    </c:if>
                    <c:if test="${dataMapper.statusMap[task.task_status] != 'pending' }">
                    	<select class="select-field task-status-select-field" name="taskStatus">
                    		<c:forEach var="status" items="${dataMapper.statusMap}">
                    			<c:if test="${status.value != 'pending'}">
                    				<option value=${status.key } <c:if test="${task.task_status == status.key }"> selected </c:if> > ${status.value}</option>
                    			</c:if>
                    		</c:forEach>
                    	</select>
                    </c:if>      
                </td>
            </tr>
        </c:forEach>
    </table>
	
	<c:if test="${dataMapper.roleMap[currentUser.role_id] != 'Developer'}">
		<h2>Pending Tasks</h2>
    	<table class="table">
        	<tr>
            	<th>Title</th>
            	<th>Status</th>
            	<th>Description</th>
            	<th>Assigned To</th>
            <th>Action</th>
        	</tr>
        	<c:forEach var="task" items="${pendingTasks}">
            	<tr data-task-id="${task.data.id }">
                	<td>${task.data.task_title}</td>
                	<td>${dataMapper.statusMap[task.data.task_status]}</td>
                	<td>${task.data.task_description}</td>
                	<td>${task.assignedEmployee.name }</td>
                	<td>
                    	<a href="tasks?action=approve&id=${task.data.id}" class="submit-button-small approve-button">Approve</a> | 
                    	<a href="tasks?action=delete&id=${task.data.id}" class="delete-button-small task-delete-button">Delete</a> | 
                    	<a href="tasks?action=edit&id=${task.data.id}" class="edit-button-small">Edit</a>
                	</td>
            	</tr>
        	</c:forEach>
    	</table>
	</c:if>
    
    
    <c:if test="${not empty allTasks }">
    	<h2>All Tasks</h2>
    	<table id="allTasksTable" class="table">
    	<thead>
    		<tr>
            	<th id="table-task-title">Title</th>
            	<th id="table-task-status">Status</th>
            	<th>Description</th>
            	<th>Assigned To</th>
            	<th>Action</th>
        </tr>
        </thead>
        <tbody id="tasks-table-body">
        <c:forEach var="task" items="${allTasks}">
            <tr data-task-id="${task.data.id }" >
                <td>${task.data.task_title}</td>
                <td>${dataMapper.statusMap[task.data.task_status]}</td>
                <td>${task.data.task_description}</td>
                <td>${task.assignedEmployee.name }</td>
                <td>
                    <a href="" class="delete-button-small task-delete-button">Delete</a> | 
                    <a href="tasks?action=edit&id=${task.data.id}" class="edit-button-small">Edit</a> | 
                    
                    <c:choose>
                    	<c:when test="${dataMapper.statusMap[task.data.task_status] == 'pending' }">
                    		<a href="tasks?action=approve&id=${task.data.id}" class="submit-button-small approve-button">Approve</a>
                    	</c:when>
                    	<c:otherwise>
                    		<select class="select-field task-status-select-field" name="taskStatus">
                    			<c:forEach var="status" items="${dataMapper.statusMap}">
                    			<c:if test="${status.value != 'pending'}">
                    				<option value=${status.key } <c:if test="${task.data.task_status == status.key }"> selected </c:if> > ${status.value}</option>
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
</div>

<script>
$(document).ready(function() {
	
	let sortTitleDirectionState = 'default';
	let sortStatusDirectionState = 'default';
	
	$('#table-task-title').on('click', function() {
		sortTitleDirectionState = toggleSortDirectionState(sortTitleDirectionState);
		sortTasks('title', sortTitleDirectionState);
	});
	
	$('#table-task-status').on('click', function() {
		sortStatusDirectionState = toggleSortDirectionState(sortStatusDirectionState);
		sortTasks('status', sortStatusDirectionState);
	});	
	
	
	function toggleSortDirectionState(currentDirectionState){
		if (currentDirectionState === 'default') {
			return 'asc';
		} else if (currentDirectionState === 'asc') {
			return 'desc';
		} else {
			return 'default';
		}
	}
	
	function sortTasks(column, sortDirection) {
		$.ajax({
			url: 'tasks',
			type: 'GET',
			data: {
				action: 'sort',
				column: column,
				sortDirection: sortDirection
			},
			success: function(response) {
				$('#tasks-table-body').html(response);
			},
			error: function(error) {
				alert('Error occured: ' + error);
			}
		});
	}
	
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
