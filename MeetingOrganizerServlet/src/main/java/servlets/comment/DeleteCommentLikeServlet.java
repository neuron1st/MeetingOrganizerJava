package servlets.comment;

import dto.user.UserModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repositories.CommentLikeRepository;
import repositories.CommentRepository;
import repositories.MeetingRepository;
import repositories.UserRepository;
import services.CommentLikeService;
import utils.BaseConnectionManager;
import utils.ConnectionManager;

import java.io.IOException;

import static utils.UrlPathGetter.*;

@WebServlet(MEETINGS + DELETE_COMMENT_LIKE)
public class DeleteCommentLikeServlet extends HttpServlet {
    private CommentLikeService commentLikeService;

    @Override
    public void init() {
        BaseConnectionManager connectionManager = new ConnectionManager();
        CommentLikeRepository commentLikeRepository = new CommentLikeRepository(connectionManager);
        UserRepository userRepository = new UserRepository(connectionManager);
        CommentRepository commentRepository = new CommentRepository(connectionManager, userRepository, new MeetingRepository(connectionManager));
        commentLikeService = new CommentLikeService(commentLikeRepository, userRepository, commentRepository);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserModel currentUser = (UserModel)request.getSession().getAttribute("user");
        Long userId = currentUser.getUserId();
        Long commentId = Long.parseLong(request.getParameter("commentId"));
        commentLikeService.removeLike(userId, commentId);
        response.sendRedirect(request.getContextPath() + "/meetings/details?id=" + request.getParameter("meetingId"));
    }
}