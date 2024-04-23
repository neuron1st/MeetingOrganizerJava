package servlets;

import dto.user.UserModel;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.MeetingLikeService;

import java.io.IOException;

import static utils.UrlPathGetter.*;

@WebServlet(MEETINGS + CREATE_MEETING_LIKE)
public class CreateMeetingLikeServlet extends HttpServlet {
    private final MeetingLikeService meetingLikeService = new MeetingLikeService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserModel currentUser = (UserModel)request.getSession().getAttribute("user");
        Long userId = currentUser.getUserId();
        Long meetingId = Long.valueOf(request.getParameter("meetingId"));
        meetingLikeService.addLike(userId, meetingId);
        response.sendRedirect(request.getContextPath() + "/meetings/details?id=" + meetingId);
    }
}
