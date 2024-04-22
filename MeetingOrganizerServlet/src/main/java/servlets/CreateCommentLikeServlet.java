package servlets;

import dto.user.UserModel;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.CommentLikeService;

import java.io.IOException;

import static utils.UrlPathGetter.CREATE_COMMENT_LIKE;
import static utils.UrlPathGetter.LOGIN;

@WebServlet(CREATE_COMMENT_LIKE)
public class CreateCommentLikeServlet extends HttpServlet {
    private final CommentLikeService commentLikeService = new CommentLikeService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object currentUserObject = request.getSession().getAttribute("user");
        if (!(currentUserObject instanceof UserModel currentUser)) {
            response.sendRedirect(request.getContextPath() + LOGIN);
            return;
        }
        Long userId = currentUser.getUserId();
        Long commentId = Long.parseLong(request.getParameter("commentId"));
        commentLikeService.addLike(userId, commentId);
        response.sendRedirect(request.getContextPath() + "/meetings/details?id=" + request.getParameter("meetingId"));
    }
}