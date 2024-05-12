package repositories;

import entity.MeetingLike;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.BaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MeetingLikeRepository {
    public final BaseConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(MeetingLikeRepository.class);
    private static final String ADD_LIKE = "INSERT INTO meeting_likes (meeting_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM meeting_likes WHERE meeting_id = ? AND user_id = ?";
    private static final String GET_LIKES_COUNT = "SELECT COUNT(1) FROM meeting_likes WHERE meeting_id = ?";

    public MeetingLikeRepository(BaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public MeetingLike create(MeetingLike like) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_LIKE)) {
            preparedStatement.setLong(1, like.getMeeting().getMeetingId());
            preparedStatement.setLong(2, like.getUser().getUserId());

            preparedStatement.executeUpdate();

            return like;
        } catch (SQLException e) {
            logger.error("Failed to add like", e);
            throw new RuntimeException("Failed to add like", e);
        }
    }

    public boolean delete(MeetingLike like) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LIKE)) {

            preparedStatement.setLong(1, like.getMeeting().getMeetingId());
            preparedStatement.setLong(2, like.getUser().getUserId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Failed to delete like", e);
            throw new RuntimeException("Failed to delete like", e);
        }
    }

    public int getLikesCount(Long meetingId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_LIKES_COUNT)) {

            preparedStatement.setLong(1, meetingId);

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
