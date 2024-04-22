<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Meeting Details</title>
</head>
<body>
    <h1>Meeting Details</h1>
    <h2>Title: ${meeting.title}</h2>
    <p>Description: ${meeting.description}</p>
    <p>Date: ${meeting.date}</p>
    <p>Like Count: ${meeting.likeCount}</p>
        <form action="<%= request.getContextPath() + "/createMeetingLike" %>" method="post">
            <input type="hidden" name="meetingId" value="${meeting.meetingId}" />
            <button type="submit">+</button>
        </form>
        <form action="<%= request.getContextPath() + "/deleteMeetingLike" %>" method="post">
            <input type="hidden" name="meetingId" value="${meeting.meetingId}" />
            <button type="submit">-</button>
        </form>
    <p>Participant Count: ${meeting.participantCount}</p>
        <form action="<%= request.getContextPath() + "/createParticipant" %>" method="post">
            <input type="hidden" name="meetingId" value="${meeting.meetingId}" />
            <button type="submit">+</button>
        </form>
        <form action="<%= request.getContextPath() + "/deleteParticipant" %>" method="post">
            <input type="hidden" name="meetingId" value="${meeting.meetingId}" />
            <button type="submit">-</button>
        </form>
    <a href="<c:url value='/meetings/participants'/>?meetingId=${meeting.meetingId}">Meeting Participants</a>
    <p>Comment Count: ${meeting.commentCount}</p>
    <a href="<%= request.getContextPath() + "/meetings" %>">Back to Meetings</a>
    <br/>
    <a href="<c:url value='/createComment?id=${meeting.meetingId}'/>">Add Comment</a>

    <h2>Comments:</h2>
    <ul>
        <c:forEach var="comment" items="${comments}">
            <li>
                <p>${comment.text}</p>
                <p>Likes: ${comment.likeCount}</p>
                <p>Date: ${comment.dateTime}</p>
                <p>User: ${comment.userName}</p>
                <form action="<%= request.getContextPath() + "/createCommentLike" %>" method="post">
                    <input type="hidden" name="commentId" value="${comment.commentId}" />
                    <input type="hidden" name="meetingId" value="${meeting.meetingId}" />
                    <button type="submit">+</button>
                </form>
                <form action="<%= request.getContextPath() + "/deleteCommentLike" %>" method="post">
                    <input type="hidden" name="commentId" value="${comment.commentId}" />
                    <input type="hidden" name="meetingId" value="${meeting.meetingId}" />
                    <button type="submit">-</button>
                </form>
            </li>
        </c:forEach>
    </ul>
</body>
</html>
