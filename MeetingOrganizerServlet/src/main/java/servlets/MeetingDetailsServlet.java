package servlets;

import dto.comment.CommentModel;
import dto.meeting.MeetingModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.CommentService;
import services.MeetingService;
import utils.JspPathCreator;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static utils.UrlPathGetter.MEETINGS;
import static utils.UrlPathGetter.DETAILS;

@WebServlet(MEETINGS+DETAILS)
public class MeetingDetailsServlet extends HttpServlet {
    private final MeetingService meetingService = new MeetingService();
    private final CommentService commentService = new CommentService();

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

            request.getRequestDispatcher(JspPathCreator.getPath("meetings-details")).forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + MEETINGS);
        }
    }
}
