<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<body>

<h1>Login</h1>

<c:if test="${not empty errorMessage}">
	<p style="color:red;"> ${errorMessage} </p>
</c:if>

<form action="${pageContext.request.contextPath}/authenticate" method="post">

	<label>Email </label>
	<input name="email" type="email" required>
	<br>
	<label>Password </label>
	<input name="password" type="password" required>
	<br><br>
	<input type="submit" value="LOGIN">
	
</form>

</body>
</html>