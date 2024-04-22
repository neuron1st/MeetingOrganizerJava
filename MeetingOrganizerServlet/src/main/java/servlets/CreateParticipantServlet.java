package servlets;

import dto.participant.CreateParticipantModel;
import dto.user.UserModel;
import entity.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.ParticipantService;
import utils.JspPathCreator;

import java.io.IOException;
import java.sql.SQLException;

import static utils.UrlPathGetter.*;
import static utils.UrlPathGetter.DETAILS;

@WebServlet(CREATE_PARTICIPANT)
public class CreateParticipantServlet extends HttpServlet {
    private final ParticipantService participantService = new ParticipantService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Object currentUserObject = request.getSession().getAttribute("user");
        if (!(currentUserObject instanceof UserModel currentUser)) {
            response.sendRedirect(request.getContextPath() + LOGIN);
            return;
        }
        Long userId = currentUser.getUserId();
        Long meetingId = Long.valueOf(request.getParameter("meetingId"));

        CreateParticipantModel createParticipantModel = CreateParticipantModel.builder()
                .meetingId(meetingId)
                .userId(userId)
                .role(Role.Participant)
                .build();
        try {
            participantService.create(createParticipantModel);
        } catch (Exception e) {
            String message = e.getMessage();
            request.setAttribute("Participant already exists", message);
        }
        response.sendRedirect(request.getContextPath() + MEETINGS + DETAILS + "?id=" + meetingId);
    }
}
