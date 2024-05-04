package servlets.meeting;

import dto.user.UserModel;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.MeetingLikeService;

import java.io.IOException;

import static utils.UrlPathGetter.*;

@WebServlet(MEETINGS + DELETE_MEETING_LIKE)
public class DeleteMeetingLikeServlet extends HttpServlet {
    private final MeetingLikeService meetingLikeService = new MeetingLikeService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserModel currentUser = (UserModel)request.getSession().getAttribute("user");
        long userId = currentUser.getUserId();
        long meetingId = Long.parseLong(request.getParameter("meetingId"));
        meetingLikeService.removeLike(userId, meetingId);
        response.sendRedirect(request.getContextPath() + "/meetings/details?id=" + meetingId);
    }
}
