<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Comment</title>
</head>
<body>
    <h1>Create Comment</h1>
    <form action="${pageContext.request.contextPath}/meetings/createComment" method="post">
        <input type="hidden" name="meetingId" value="${param.id}" />
        <textarea name="text" rows="5" cols="50" placeholder="Enter your comment here" required></textarea><br/>
        <input type="submit" value="Submit" />
    </form>
</body>
</html>
