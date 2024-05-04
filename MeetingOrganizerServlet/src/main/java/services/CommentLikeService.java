package services;

import entity.CommentLike;
import repositories.CommentLikeRepository;
import repositories.CommentRepository;
import repositories.MeetingRepository;
import repositories.UserRepository;

public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository = new CommentLikeRepository();
    private final UserRepository userRepository = new UserRepository();
    private final CommentRepository commentRepository = new CommentRepository(userRepository, new MeetingRepository());

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
