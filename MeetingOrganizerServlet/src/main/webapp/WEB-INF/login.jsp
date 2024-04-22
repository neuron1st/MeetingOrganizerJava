<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<h1>Login</h1>
<% if (request.getAttribute("message") != null) { %>
    <p style="color: red;"><%= request.getAttribute("message") %></p>
<% } %>
<form method="post" action="<%= request.getContextPath() + "/login" %>">
    <label for="email">Email:</label><br>
    <input type="email" id="email" name="email" required><br><br>

    <label for="password">Password:</label><br>
    <input type="password" id="password" name="password" required><br><br>

    <input type="submit" value="Login">
</form>

<br>
<a href="<%= request.getContextPath() + "/registration" %>">Register</a>
</body>
</html>
