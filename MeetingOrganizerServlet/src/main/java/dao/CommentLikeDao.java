package dao;

import entity.CommentLike;
import utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CommentLikeDao implements Dao<Long, CommentLike> {
    private static final String ADD_LIKE = "INSERT INTO comment_likes (comment_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM comment_likes WHERE comment_id = ? AND user_id = ?";
    private static final String GET_LIKES_COUNT = "SELECT COUNT(*) FROM comment_likes WHERE comment_id = ?";

    @Override
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

    @Override
    public List<CommentLike> getAll() {
        throw new UnsupportedOperationException("Getting all likes is not supported");
    }

    @Override
    public Optional<CommentLike> getById(Long id) {
        throw new UnsupportedOperationException("Getting like by ID is not supported");
    }

    @Override
    public boolean update(CommentLike like) {
        throw new UnsupportedOperationException("Updating like is not supported");
    }

    @Override
    public boolean delete(Long id) {
        throw new UnsupportedOperationException("Deleting like by ID is not supported");
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
