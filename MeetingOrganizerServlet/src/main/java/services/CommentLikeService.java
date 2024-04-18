package services;

import dao.CommentDao;
import dao.CommentLikeDao;
import dao.UserDao;
import entity.CommentLike;
import utils.DaoManager;

public class CommentLikeService {
    private final CommentLikeDao commentLikeDao = DaoManager.getCommentLikeDao();
    private final UserDao userDao = DaoManager.getUserDao();
    private final CommentDao commentDao = DaoManager.getCommentDao();

    public void addLike(Long userId, Long commentId) {
        commentLikeDao.create(CommentLike.builder()
                .user(userDao.getById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId)))
                .comment(commentDao.getById(commentId)
                        .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId)))
                .build());
    }

    public boolean removeLike(Long userId, Long commentId) {
        return commentLikeDao.delete(CommentLike.builder()
                .user(userDao.getById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId)))
                .comment(commentDao.getById(commentId)
                        .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId)))
                .build());
    }
}
