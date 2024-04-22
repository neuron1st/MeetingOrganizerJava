package servlets;

import dto.participant.CreateParticipantModel;
import dto.user.UserModel;
import entity.Role;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.ParticipantService;

import java.io.IOException;

import static utils.UrlPathGetter.*;
import static utils.UrlPathGetter.DETAILS;

@WebServlet(DELETE_PARTICIPANT)
public class DeleteParticipantServlet extends HttpServlet {
    private final ParticipantService participantService = new ParticipantService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object currentUserObject = request.getSession().getAttribute("user");
        if (!(currentUserObject instanceof UserModel currentUser)) {
            response.sendRedirect(request.getContextPath() + LOGIN);
            return;
        }
        Long userId = currentUser.getUserId();
        Long meetingId = Long.valueOf(request.getParameter("meetingId"));

        participantService.delete(meetingId, userId);

        response.sendRedirect(request.getContextPath() + MEETINGS + DETAILS + "?id=" + meetingId);
    }

}
