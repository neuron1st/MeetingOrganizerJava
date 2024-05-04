package servlets.meeting;

import dto.meeting.MeetingModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.MeetingService;
import utils.JspPathCreator;

import java.io.IOException;
import java.util.List;

import static utils.UrlPathGetter.MEETINGS;

@WebServlet(MEETINGS)
public class MeetingsServlet extends HttpServlet {
    private final MeetingService meetingService = new MeetingService();

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
