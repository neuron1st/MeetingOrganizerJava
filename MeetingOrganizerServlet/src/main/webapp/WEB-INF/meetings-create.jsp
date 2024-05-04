<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Meeting</title>
</head>
<body>
    <h1>Create Meeting</h1>
    <form action="${pageContext.request.contextPath}/meetings/create" method="post">
        <label for="title">Title:</label>
        <input type="text" id="title" name="title" required><br>
        <label for="description">Description:</label>
        <textarea id="description" name="description" required></textarea><br>
        <label for="date">Date:</label>
        <input type="datetime-local" id="date" name="date" required><br>
        <input type="submit" value="Create Meeting">
    </form>
</body>
</html>
