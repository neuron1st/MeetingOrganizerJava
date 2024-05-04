package repositories;

import entity.CommentLike;
import utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentLikeRepository {
    private static final String ADD_LIKE = "INSERT INTO comment_likes (comment_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM comment_likes WHERE comment_id = ? AND user_id = ?";
    private static final String GET_LIKES_COUNT = "SELECT COUNT(1) FROM comment_likes WHERE comment_id = ?";

    public CommentLike create(CommentLike like) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_LIKE)) {

            preparedStatement.setLong(1, like.getComment().getCommentId());
            preparedStatement.setLong(2, like.getUser().getUserId());

            preparedStatement.executeUpdate();

            return like;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add like", e);
        }
    }

    public boolean delete(CommentLike like) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LIKE)) {

            preparedStatement.setLong(1, like.getComment().getCommentId());
            preparedStatement.setLong(2, like.getUser().getUserId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete like", e);
        }
    }

    public int getLikesCount(Long commentId) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_LIKES_COUNT)) {

            preparedStatement.setLong(1, commentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get likes count", e);
        }

        return 0;
    }
}
