package servlets.meeting;

import dto.comment.CommentModel;
import dto.meeting.MeetingModel;
import dto.participant.ParticipantModel;
import dto.user.UserModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappers.comment.CommentMapper;
import mappers.comment.CreateCommentMapper;
import mappers.meeting.CreateMeetingMapper;
import mappers.meeting.MeetingMapper;
import mappers.participant.CreateParticipantMapper;
import mappers.participant.ParticipantMapper;
import repositories.CommentLikeRepository;
import repositories.CommentRepository;
import repositories.MeetingLikeRepository;
import repositories.MeetingRepository;
import repositories.ParticipantRepository;
import repositories.UserRepository;
import services.CommentService;
import services.MeetingService;
import services.ParticipantService;
import utils.BaseConnectionManager;
import utils.ConnectionManager;
import utils.JspPathCreator;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static utils.UrlPathGetter.DETAILS;
import static utils.UrlPathGetter.MEETINGS;

@WebServlet(MEETINGS+DETAILS)
public class MeetingDetailsServlet extends HttpServlet {
    private MeetingService meetingService;
    private CommentService commentService;
    private  ParticipantService participantService;
    @Override
    public void init() {
        BaseConnectionManager connectionManager = new ConnectionManager();
        UserRepository userRepository = new UserRepository(connectionManager);
        MeetingRepository meetingRepository = new MeetingRepository(connectionManager);
        CommentRepository commentRepository = new CommentRepository(connectionManager, userRepository, meetingRepository);
        ParticipantRepository participantRepository = new ParticipantRepository(connectionManager, userRepository, meetingRepository);
        MeetingLikeRepository meetingLikeRepository = new MeetingLikeRepository(connectionManager);
        CommentLikeRepository commentLikeRepository = new CommentLikeRepository(connectionManager);
        MeetingMapper meetingMapper = new MeetingMapper();
        CreateMeetingMapper createMeetingMapper = new CreateMeetingMapper();
        CommentMapper commentMapper = new CommentMapper();
        CreateCommentMapper createCommentMapper = new CreateCommentMapper();
        ParticipantMapper participantMapper = new ParticipantMapper();
        CreateParticipantMapper createParticipantMapper = new CreateParticipantMapper();

        commentService = new CommentService(commentRepository, commentLikeRepository, commentMapper, createCommentMapper);

        meetingService = new MeetingService(meetingRepository, commentRepository, participantRepository, meetingLikeRepository, meetingMapper, createMeetingMapper);

        participantService = new ParticipantService(participantRepository, participantMapper, createParticipantMapper);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String meetingIdString = request.getParameter("id");
        if (meetingIdString == null || meetingIdString.isEmpty()) {
            response.sendRedirect(request.getContextPath() + MEETINGS);
            return;
        }

        Long meetingId = Long.parseLong(meetingIdString);
        Optional<MeetingModel> meetingOptional = meetingService.getById(meetingId);
        if (meetingOptional.isPresent()) {
            MeetingModel meeting = meetingOptional.get();
            request.setAttribute("meeting", meeting);

            List<CommentModel> comments = commentService.getByMeetingId(meetingId);
            request.setAttribute("comments", comments);

            UserModel user = (UserModel) request.getSession().getAttribute("user");

            if (user != null) {
                Optional<ParticipantModel> participant = participantService.getById(meetingId, user.getUserId());
                participant.ifPresent(participantModel -> request.setAttribute("userRole", participantModel.getRole().toString()));
            }

            request.getRequestDispatcher(JspPathCreator.getPath("meetings-details")).forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + MEETINGS);
        }
    }
}
