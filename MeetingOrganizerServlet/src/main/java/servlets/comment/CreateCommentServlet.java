package servlets.comment;

import dto.comment.CreateCommentModel;
import dto.user.UserModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mappers.comment.CommentMapper;
import mappers.comment.CreateCommentMapper;
import repositories.CommentLikeRepository;
import repositories.CommentRepository;
import repositories.MeetingRepository;
import repositories.UserRepository;
import services.CommentService;
import utils.BaseConnectionManager;
import utils.ConnectionManager;
import utils.JspPathCreator;
import validators.CommentValidator;

import java.io.IOException;
import java.time.LocalDateTime;

import static utils.UrlPathGetter.*;

@WebServlet(MEETINGS + CREATE_COMMENT)
public class CreateCommentServlet extends HttpServlet {
    private CommentService commentService;
    private final CommentValidator commentValidator = new CommentValidator();

    @Override
    public void init() {
        BaseConnectionManager connectionManager = new ConnectionManager();
        UserRepository userRepository = new UserRepository(connectionManager);
        MeetingRepository meetingRepository = new MeetingRepository(connectionManager);

        CommentRepository commentRepository = new CommentRepository(connectionManager, userRepository, meetingRepository);
        CommentLikeRepository commentLikeRepository = new CommentLikeRepository(connectionManager);
        CommentMapper commentMapper = new CommentMapper();
        CreateCommentMapper createCommentMapper = new CreateCommentMapper();

        commentService = new CommentService(commentRepository, commentLikeRepository, commentMapper, createCommentMapper);
    }

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
