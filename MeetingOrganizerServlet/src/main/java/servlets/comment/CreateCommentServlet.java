package servlets.comment;

import dto.comment.CreateCommentModel;
import dto.user.UserModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.CommentService;
import utils.JspPathCreator;
import validators.CommentValidator;

import java.io.IOException;
import java.time.LocalDateTime;

import static utils.UrlPathGetter.*;

@WebServlet(MEETINGS + CREATE_COMMENT)
public class CreateCommentServlet extends HttpServlet {
    private final CommentService commentService = new CommentService();
    private final CommentValidator commentValidator = new CommentValidator();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(JspPathCreator.getPath("comments-create")).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserModel currentUser = (UserModel)request.getSession().getAttribute("user");
        String text = request.getParameter("text");
        long meetingId = Long.parseLong(request.getParameter("meetingId"));

        CreateCommentModel createModel = CreateCommentModel.builder()
                .text(text)
                .dateTime(LocalDateTime.now())
                .userId(currentUser.getUserId())
                .meetingId(meetingId)
                .build();

        try {
            commentValidator.validate(createModel);
        } catch (IllegalArgumentException ex) {
            request.setAttribute("text", text);
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher(JspPathCreator.getPath("comments-create")).forward(request, response);
            return;
        }

        commentService.create(createModel);
        response.sendRedirect(request.getContextPath() + MEETINGS + DETAILS + "?id=" + meetingId);
    }
}
