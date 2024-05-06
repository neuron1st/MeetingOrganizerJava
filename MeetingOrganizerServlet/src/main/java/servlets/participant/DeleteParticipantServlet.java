package servlets.participant;

import dto.user.UserModel;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappers.participant.CreateParticipantMapper;
import mappers.participant.ParticipantMapper;
import repositories.MeetingRepository;
import repositories.ParticipantRepository;
import repositories.UserRepository;
import services.ParticipantService;
import utils.BaseConnectionManager;
import utils.ConnectionManager;

import java.io.IOException;

import static utils.UrlPathGetter.*;
import static utils.UrlPathGetter.DETAILS;

@WebServlet(MEETINGS + DELETE_PARTICIPANT)
public class DeleteParticipantServlet extends HttpServlet {
    private ParticipantService participantService;
    @Override
    public void init() {
        BaseConnectionManager connectionManager = new ConnectionManager();
        UserRepository userRepository = new UserRepository(connectionManager);
        MeetingRepository meetingRepository = new MeetingRepository(connectionManager);
        ParticipantRepository participantRepository = new ParticipantRepository(connectionManager, userRepository, meetingRepository);
        ParticipantMapper participantMapper = new ParticipantMapper();
        CreateParticipantMapper createParticipantMapper = new CreateParticipantMapper();

        participantService = new ParticipantService(participantRepository, participantMapper, createParticipantMapper);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserModel currentUser = (UserModel)request.getSession().getAttribute("user");
        long userId = currentUser.getUserId();
        long meetingId = Long.parseLong(request.getParameter("meetingId"));

        participantService.delete(meetingId, userId);

        response.sendRedirect(request.getContextPath() + MEETINGS + DETAILS + "?id=" + meetingId);
    }

}
