<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Meeting Participants</title>
</head>
<body>
    <h1>Meeting Participants</h1>
    <a href="<c:url value='/meetings/details?id=${meetingId}'/>">Back to meeting</a>
    <table border="1">
        <thead>
            <tr>
                <th>Name</th>
                <th>Role</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="participant" items="${participants}">
                <tr>
                    <td>${participant.userName}</td>
                    <td>${participant.role.name()}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
