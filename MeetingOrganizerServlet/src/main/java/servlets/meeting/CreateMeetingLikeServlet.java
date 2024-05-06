package servlets.meeting;

import dto.user.UserModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repositories.MeetingLikeRepository;
import repositories.MeetingRepository;
import repositories.UserRepository;
import services.MeetingLikeService;
import utils.BaseConnectionManager;
import utils.ConnectionManager;

import java.io.IOException;

import static utils.UrlPathGetter.CREATE_MEETING_LIKE;
import static utils.UrlPathGetter.MEETINGS;

@WebServlet(MEETINGS + CREATE_MEETING_LIKE)
public class CreateMeetingLikeServlet extends HttpServlet {
    private MeetingLikeService meetingLikeService;

    @Override
    public void init() {
        BaseConnectionManager connectionManager = new ConnectionManager();
        UserRepository userRepository = new UserRepository(connectionManager);
        MeetingRepository meetingRepository = new MeetingRepository(connectionManager);
        MeetingLikeRepository meetingLikeRepository = new MeetingLikeRepository(connectionManager);

        meetingLikeService = new MeetingLikeService(meetingLikeRepository, userRepository, meetingRepository);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserModel currentUser = (UserModel)request.getSession().getAttribute("user");
        long userId = currentUser.getUserId();
        long meetingId = Long.parseLong(request.getParameter("meetingId"));
        meetingLikeService.addLike(userId, meetingId);
        response.sendRedirect(request.getContextPath() + "/meetings/details?id=" + meetingId);
    }
}
