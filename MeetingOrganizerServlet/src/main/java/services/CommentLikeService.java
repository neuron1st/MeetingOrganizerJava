package services;

import repositories.CommentRepository;
import repositories.CommentLikeRepository;
import repositories.UserRepository;
import entity.CommentLike;
import utils.RepositoryManager;

public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository = RepositoryManager.getCommentLikeRepository();
    private final UserRepository userRepository = RepositoryManager.getUserRepository();
    private final CommentRepository commentRepository = RepositoryManager.getCommentRepository();

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
