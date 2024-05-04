<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Meeting</title>
</head>
<body>
    <h1>Create Meeting</h1>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color: red;"><%= request.getAttribute("error") %></p>
    <% } %>
    <form action="${pageContext.request.contextPath}/meetings/create" method="post">
        <label for="title">Title:</label>
        <input type="text" id="title" name="title" value="${not empty title ? title : ''}" required><br>
        <label for="description">Description:</label>
        <textarea id="description" name="description">${not empty description ? description : ''}</textarea><br>
        <label for="date">Date:</label>
        <input type="datetime-local" id="date" name="date" value="${not empty date ? date : ''}" required><br>
        <input type="submit" value="Create Meeting">
    </form>
</body>
</html>
