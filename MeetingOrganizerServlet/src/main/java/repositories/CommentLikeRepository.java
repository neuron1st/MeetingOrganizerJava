package repositories;

import entity.CommentLike;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.BaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentLikeRepository {
    public final BaseConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(CommentLikeRepository.class);
    private static final String ADD_LIKE = "INSERT INTO comment_likes (comment_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM comment_likes WHERE comment_id = ? AND user_id = ?";
    private static final String GET_LIKES_COUNT = "SELECT COUNT(1) FROM comment_likes WHERE comment_id = ?";

    public CommentLikeRepository(BaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public CommentLike create(CommentLike like) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_LIKE)) {

            preparedStatement.setLong(1, like.getComment().getCommentId());
            preparedStatement.setLong(2, like.getUser().getUserId());

            preparedStatement.executeUpdate();

            return like;
        } catch (SQLException e) {
            logger.error("Failed to add like", e);
            throw new RuntimeException("Failed to add like", e);
        }
    }

    public boolean delete(CommentLike like) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LIKE)) {

            preparedStatement.setLong(1, like.getComment().getCommentId());
            preparedStatement.setLong(2, like.getUser().getUserId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Failed to delete like", e);
            throw new RuntimeException("Failed to delete like", e);
        }
    }

    public int getLikesCount(long commentId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_LIKES_COUNT)) {

            preparedStatement.setLong(1, commentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to get likes count", e);
            throw new RuntimeException("Failed to get likes count", e);
        }

        return 0;
    }
}
