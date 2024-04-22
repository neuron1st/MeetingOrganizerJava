package servlets;

import dto.user.UserModel;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.MeetingLikeService;

import java.io.IOException;

import static utils.UrlPathGetter.DELETE_MEETING_LIKE;
import static utils.UrlPathGetter.LOGIN;

@WebServlet(DELETE_MEETING_LIKE)
public class DeleteMeetingLikeServlet extends HttpServlet {
    private final MeetingLikeService meetingLikeService = new MeetingLikeService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object currentUserObject = request.getSession().getAttribute("user");
        if (!(currentUserObject instanceof UserModel currentUser)) {
            response.sendRedirect(request.getContextPath() + LOGIN);
            return;
        }
        Long userId = currentUser.getUserId();
        Long meetingId = Long.valueOf(request.getParameter("meetingId"));
        meetingLikeService.removeLike(userId, meetingId);
        response.sendRedirect(request.getContextPath() + "/meetings/details?id=" + meetingId);
    }
}
