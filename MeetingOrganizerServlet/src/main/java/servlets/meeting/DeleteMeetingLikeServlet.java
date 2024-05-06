package servlets.meeting;

import dto.user.UserModel;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappers.meeting.CreateMeetingMapper;
import mappers.meeting.MeetingMapper;
import mappers.participant.CreateParticipantMapper;
import mappers.participant.ParticipantMapper;
import repositories.CommentRepository;
import repositories.MeetingLikeRepository;
import repositories.MeetingRepository;
import repositories.ParticipantRepository;
import repositories.UserRepository;
import services.MeetingLikeService;
import services.MeetingService;
import services.ParticipantService;
import utils.BaseConnectionManager;
import utils.ConnectionManager;

import java.io.IOException;

import static utils.UrlPathGetter.*;

@WebServlet(MEETINGS + DELETE_MEETING_LIKE)
public class DeleteMeetingLikeServlet extends HttpServlet {
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
        meetingLikeService.removeLike(userId, meetingId);
        response.sendRedirect(request.getContextPath() + "/meetings/details?id=" + meetingId);
    }
}
