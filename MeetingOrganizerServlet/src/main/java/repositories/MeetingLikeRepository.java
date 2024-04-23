package repositories;

import entity.MeetingLike;
import utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MeetingLikeRepository {
    private static final String ADD_LIKE = "INSERT INTO meeting_likes (meeting_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM meeting_likes WHERE meeting_id = ? AND user_id = ?";
    private static final String GET_LIKES_COUNT = "SELECT COUNT(*) FROM meeting_likes WHERE meeting_id = ?";

    public MeetingLike create(MeetingLike like) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_LIKE)) {

            preparedStatement.setLong(1, like.getMeeting().getMeetingId());
            preparedStatement.setLong(2, like.getUser().getUserId());

            preparedStatement.executeUpdate();

            return like;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add like", e);
        }
    }

    public boolean delete(MeetingLike like) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LIKE)) {

            preparedStatement.setLong(1, like.getMeeting().getMeetingId());
            preparedStatement.setLong(2, like.getUser().getUserId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete like", e);
        }
    }

    public int getLikesCount(Long meetingId) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_LIKES_COUNT)) {

            preparedStatement.setLong(1, meetingId);

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
