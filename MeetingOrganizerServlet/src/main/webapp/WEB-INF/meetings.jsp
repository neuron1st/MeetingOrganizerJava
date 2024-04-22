<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Meetings</title>
</head>
<body>
    <h1>Meetings</h1>
    <a href="<c:url value='/createMeeting'/>">Create Meeting</a>
    <form action="<c:url value='/meetings'/>" method="get">
        <label for="searchTitle">Search by Title:</label>
        <input type="text" id="searchTitle" name="searchTitle" value="${param.searchTitle}">
        <input type="submit" value="Search">
    </form>
    <c:choose>
        <c:when test="${not empty requestScope.meetings}">
            <ul>
                <c:forEach var="meeting" items="${meetings}">
                    <c:if test="${empty param.searchTitle or meeting.title.toLowerCase().contains(param.searchTitle.toLowerCase())}">
                        <li>
                            <h2><a href="<c:url value='/meetings/details?id=${meeting.meetingId}'/>">${meeting.title}</a></h2>
                            <p>${meeting.description}</p>
                            <p>Date: ${meeting.date}</p>
                            <p>Likes: ${meeting.likeCount}</p>
                            <p>Participants: ${meeting.participantCount}</p>
                            <p>Comments: ${meeting.commentCount}</p>
                        </li>
                    </c:if>
                </c:forEach>

            </ul>
        </c:when>
        <c:otherwise>
            <p>No meetings found.</p>
        </c:otherwise>
    </c:choose>
</body>

</html>
