package servlets.participant;

import dto.participant.ParticipantModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.ParticipantService;
import utils.JspPathCreator;

import java.io.IOException;
import java.util.List;

import static utils.UrlPathGetter.MEETINGS;
import static utils.UrlPathGetter.PARTICIPANTS;

@WebServlet(MEETINGS + PARTICIPANTS)
public class ParticipantByMeetingIdServlet extends HttpServlet {
    private final ParticipantService participantService = new ParticipantService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long meetingId = Long.parseLong(request.getParameter("meetingId"));
        List<ParticipantModel> models = participantService.getAllByMeetingId(meetingId);
        request.setAttribute("participants", models);
        request.setAttribute("meetingId", meetingId);
        request.getRequestDispatcher(JspPathCreator.getPath("participants")).forward(request, response);
    }
}
