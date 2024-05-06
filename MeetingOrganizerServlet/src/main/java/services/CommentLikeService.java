package services;

import entity.CommentLike;
import repositories.CommentLikeRepository;
import repositories.CommentRepository;
import repositories.MeetingRepository;
import repositories.UserRepository;
import utils.BaseConnectionManager;
import utils.ConnectionManager;

public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public CommentLikeService(
            CommentLikeRepository commentLikeRepository,
            UserRepository userRepository,
            CommentRepository commentRepository
    ) {
        this.commentLikeRepository = commentLikeRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }


    public void addLike(Long userId, Long commentId) {
        commentLikeRepository.create(CommentLike.builder()
                .user(userRepository.getById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId)))
                .comment(commentRepository.getById(commentId)
                        .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId)))
                .build());
    }

    public void removeLike(Long userId, Long commentId) {
        commentLikeRepository.delete(CommentLike.builder()
                .user(userRepository.getById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId)))
                .comment(commentRepository.getById(commentId)
                        .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId)))
                .build());
    }
}
