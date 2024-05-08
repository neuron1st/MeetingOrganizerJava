package services;

import entity.CommentLike;
import repositories.CommentLikeRepository;
import repositories.CommentRepository;
import repositories.UserRepository;

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


    public void addLike(long userId, long commentId) {
        commentLikeRepository.create(CommentLike.builder()
                .user(userRepository.getById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId)))
                .comment(commentRepository.getById(commentId)
                        .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId)))
                .build());
    }

    public void removeLike(long userId, long commentId) {
        commentLikeRepository.delete(CommentLike.builder()
                .user(userRepository.getById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId)))
                .comment(commentRepository.getById(commentId)
                        .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId)))
                .build());
    }
}
