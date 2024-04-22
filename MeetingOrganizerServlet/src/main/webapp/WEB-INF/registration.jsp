<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<h1>Registration</h1>
<% if (request.getAttribute("message") != null) { %>
    <p style="color: red;"><%= request.getAttribute("message") %></p>
<% } %>
<form method="post" action="<%= request.getContextPath() + "/registration" %>">
    <label for="fullName">Full Name:</label><br>
    <input type="text" id="fullName" name="fullName" required><br><br>

    <label for="email">Email:</label><br>
    <input type="email" id="email" name="email" required><br><br>

    <label for="password">Password:</label><br>
    <input type="password" id="password" name="password" required><br><br>

    <input type="submit" value="Register">
</form>

<br>
<a href="<%= request.getContextPath() + "/login" %>">Login</a>
</body>
</html>
