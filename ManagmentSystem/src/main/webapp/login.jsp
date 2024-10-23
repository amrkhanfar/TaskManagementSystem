<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
<br>
<div class="container">
    <h1>Login</h1>

    <c:if test="${not empty errorMessage}">
        <p class="error-message">${errorMessage}</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/authenticate" method="post" class="form">

        <label>Email</label>
        <input name="email" type="email" required class="input-field">
        <br>
        <label>Password</label>
        <input name="password" type="password" required class="input-field">
        <br><br>
        <input type="submit" value="LOGIN" class="button">
    </form>
</div>

</body>
</html>
