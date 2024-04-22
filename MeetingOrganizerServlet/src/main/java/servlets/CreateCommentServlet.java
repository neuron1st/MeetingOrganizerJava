package servlets;

import dto.comment.CommentModel;
import dto.comment.CreateCommentModel;
import dto.user.UserModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.CommentService;
import utils.JspPathCreator;

import java.io.IOException;
import java.time.LocalDateTime;

import static utils.UrlPathGetter.*;

@WebServlet(CREATE_COMMENT)
public class CreateCommentServlet extends HttpServlet {
    private final CommentService commentService = new CommentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(JspPathCreator.getPath("createComment")).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object currentUserObject = request.getSession().getAttribute("user");
        if (!(currentUserObject instanceof UserModel currentUser)) {
            response.sendRedirect(request.getContextPath() + LOGIN);
            return;
        }
        String text = request.getParameter("text");
        Long meetingId = Long.parseLong(request.getParameter("meetingId"));

        CreateCommentModel createModel = CreateCommentModel.builder()
                .text(text)
                .dateTime(LocalDateTime.now())
                .userId(currentUser.getUserId())
                .meetingId(meetingId)
                .build();

        CommentModel commentModel = commentService.create(createModel);
        response.sendRedirect(request.getContextPath() + MEETINGS + DETAILS + "?id=" + meetingId);
    }
}
