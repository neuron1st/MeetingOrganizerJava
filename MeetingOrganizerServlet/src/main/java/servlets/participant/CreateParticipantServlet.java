package servlets.participant;

import dto.participant.CreateParticipantModel;
import dto.user.UserModel;
import entity.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.ParticipantService;

import java.io.IOException;

import static utils.UrlPathGetter.*;
import static utils.UrlPathGetter.DETAILS;

@WebServlet(MEETINGS + CREATE_PARTICIPANT)
public class CreateParticipantServlet extends HttpServlet {
    private final ParticipantService participantService = new ParticipantService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserModel currentUser = (UserModel)request.getSession().getAttribute("user");
        long userId = currentUser.getUserId();
        long meetingId = Long.parseLong(request.getParameter("meetingId"));

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
