<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Employee Form</title>
</head>
<body>

<c:if test="${not empty notification }">
	<p style="color=green;">${notification }</p>
</c:if>
<c:if test="${not empty errorMessage }">
	<p style="color:red;"> ${errorMessage} </p>
</c:if>

<c:choose>
	<c:when test="${action == 'add' }"> Add new Employee </c:when>
	<c:when test="${action == 'edit' }"> Edit Employee </c:when>
</c:choose>

<form action="${pageContext.request.contextPath}/employees" method="post">

<input name ="action" type="hidden" value="${action }"/>
<input name="id" type="hidden" value ="${employee.id }">
<label>Name </label>
<input name="name" type="text" value="${employee.name }" required"/>
<br>
<label>Email </label>
<input name="email" type="text" value="${employee.email }" required"/>
<br/>
<label>Password </label>
<input name="password" type="password" required/>
<br/>
<label>Role </label>
<select name="role">
	<c:forEach var="role" items="${roles }">
		<option value="${role.id }" <c:if test="${role.id == employee.role_id }">selected</c:if>  >${role.role_name}</option>
	</c:forEach>
</select>
<br>
<label>Team </label>
<select name="team">
	<c:forEach var="team" items="${teams }">
		<option value="${team.id }" <c:if test="${employee.team_id == team.id }">selected</c:if> >${team.team_name } -ID: ${team.id }-</option>
	</c:forEach>
</select>

<br>
<input type="submit" value="SUBMIT" />
<a href="${pageContext.request.contextPath}/employees">  CANCEL</a>

</form>

</body>
</html>