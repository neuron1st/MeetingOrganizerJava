package servlets.meeting;

import dto.meeting.CreateMeetingModel;
import dto.meeting.MeetingModel;
import dto.participant.CreateParticipantModel;
import dto.user.UserModel;
import entity.Role;
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
import validators.MeetingValidator;

import java.io.IOException;
import java.time.LocalDateTime;

import static utils.UrlPathGetter.CREATE_MEETING;
import static utils.UrlPathGetter.DETAILS;
import static utils.UrlPathGetter.MEETINGS;

@WebServlet(MEETINGS + CREATE_MEETING)
public class CreateMeetingServlet extends HttpServlet {
    private MeetingService meetingService;
    private ParticipantService participantService;

    private final MeetingValidator meetingValidator = new MeetingValidator();
    @Override
    public void init() {
        BaseConnectionManager connectionManager = new ConnectionManager();
        UserRepository userRepository = new UserRepository(connectionManager);
        MeetingRepository meetingRepository = new MeetingRepository(connectionManager);
        CommentRepository commentRepository = new CommentRepository(connectionManager, userRepository, meetingRepository);
        ParticipantRepository participantRepository = new ParticipantRepository(connectionManager, userRepository, meetingRepository);
        MeetingLikeRepository meetingLikeRepository = new MeetingLikeRepository(connectionManager);
        ParticipantMapper participantMapper = new ParticipantMapper();
        CreateParticipantMapper createParticipantMapper = new CreateParticipantMapper();
        MeetingMapper meetingMapper = new MeetingMapper();
        CreateMeetingMapper createMeetingMapper = new CreateMeetingMapper();

        participantService = new ParticipantService(participantRepository, participantMapper, createParticipantMapper);

        meetingService = new MeetingService(meetingRepository, commentRepository, participantRepository, meetingLikeRepository, meetingMapper, createMeetingMapper);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(JspPathCreator.getPath("meetings-create")).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        LocalDateTime date = LocalDateTime.parse(request.getParameter("date"));

        CreateMeetingModel createModel = CreateMeetingModel.builder()
                .title(title)
                .description(description)
                .date(date)
                .build();

        try {
            meetingValidator.validate(createModel);
        } catch (IllegalArgumentException ex) {
            request.setAttribute("title", title);
            request.setAttribute("description", description);
            request.setAttribute("date", request.getParameter("date"));
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher(JspPathCreator.getPath("meetings-create")).forward(request, response);
            return;
        }

        MeetingModel meetingModel = meetingService.create(createModel);

        CreateParticipantModel participantModel = CreateParticipantModel.builder()
                .userId(((UserModel)request.getSession().getAttribute("user")).getUserId())
                .meetingId(meetingModel.getMeetingId())
                .role(Role.Admin)
                .build();

        participantService.create(participantModel);

        response.sendRedirect(request.getContextPath() + MEETINGS + DETAILS + "?id=" + meetingModel.getMeetingId());
    }
}
