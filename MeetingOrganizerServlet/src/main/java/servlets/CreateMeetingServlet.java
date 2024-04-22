package servlets;

import dto.meeting.CreateMeetingModel;
import dto.meeting.MeetingModel;
import dto.participant.CreateParticipantModel;
import dto.participant.ParticipantModel;
import dto.user.UserModel;
import entity.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.MeetingService;
import services.ParticipantService;
import utils.JspPathCreator;

import java.io.IOException;
import java.time.LocalDateTime;

import static utils.UrlPathGetter.*;

@WebServlet(CREATE_MEETING)
public class CreateMeetingServlet extends HttpServlet {
    private final MeetingService meetingService = new MeetingService();
    private final ParticipantService participantService = new ParticipantService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(JspPathCreator.getPath("createMeeting")).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object currentUserObject = request.getSession().getAttribute("user");
        if (!(currentUserObject instanceof UserModel currentUser)) {
            response.sendRedirect(request.getContextPath() + LOGIN);
            return;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        LocalDateTime date = LocalDateTime.parse(request.getParameter("date"));

        CreateMeetingModel createModel = CreateMeetingModel.builder()
                .title(title)
                .description(description)
                .date(date)
                .build();

        MeetingModel meetingModel = meetingService.create(createModel);

        CreateParticipantModel participantModel = CreateParticipantModel.builder()
                .userId(currentUser.getUserId())
                .meetingId(meetingModel.getMeetingId())
                .role(Role.Admin)
                .build();

        participantService.create(participantModel);

        response.sendRedirect(request.getContextPath() + MEETINGS + DETAILS + "?id=" + meetingModel.getMeetingId());
    }
}
