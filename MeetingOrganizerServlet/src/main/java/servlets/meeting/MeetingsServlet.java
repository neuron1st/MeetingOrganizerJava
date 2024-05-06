package servlets.meeting;

import dto.meeting.MeetingModel;
import jakarta.servlet.ServletException;
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
import services.MeetingService;
import services.ParticipantService;
import utils.BaseConnectionManager;
import utils.ConnectionManager;
import utils.JspPathCreator;

import java.io.IOException;
import java.util.List;

import static utils.UrlPathGetter.MEETINGS;

@WebServlet(MEETINGS)
public class MeetingsServlet extends HttpServlet {
    private MeetingService meetingService;
    @Override
    public void init() {
        BaseConnectionManager connectionManager = new ConnectionManager();
        UserRepository userRepository = new UserRepository(connectionManager);
        MeetingRepository meetingRepository = new MeetingRepository(connectionManager);
        CommentRepository commentRepository = new CommentRepository(connectionManager, userRepository, meetingRepository);
        ParticipantRepository participantRepository = new ParticipantRepository(connectionManager, userRepository, meetingRepository);
        MeetingLikeRepository meetingLikeRepository = new MeetingLikeRepository(connectionManager);
        MeetingMapper meetingMapper = new MeetingMapper();
        CreateMeetingMapper createMeetingMapper = new CreateMeetingMapper();

        meetingService = new MeetingService(meetingRepository, commentRepository, participantRepository, meetingLikeRepository, meetingMapper, createMeetingMapper);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTitle = request.getParameter("searchTitle");
        List<MeetingModel> models;
        if (searchTitle != null && !searchTitle.isEmpty()) {
            models = meetingService.getByTitle(searchTitle);
        } else {
            models = meetingService.getAll();
        }
        request.setAttribute("meetings", models);
        request.getRequestDispatcher(JspPathCreator.getPath("meetings")).forward(request, response);
    }

}
