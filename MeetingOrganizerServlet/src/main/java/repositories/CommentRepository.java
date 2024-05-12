package repositories;

import entity.Comment;
import entity.Meeting;
import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.BaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentRepository implements Repository<Long, Comment> {
    public final BaseConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(CommentRepository.class);
    private static final String CREATE_COMMENT = "INSERT INTO comments (text, creation_date, user_id, meeting_id) " +
            "VALUES (?, ?, ?, ?)";
    private static final String GET_ALL_COMMENTS = "SELECT comment_id, text, creation_date, user_id, meeting_id " +
            "FROM comments";
    private static final String GET_ALL_BY_MEETING_ID = "SELECT comment_id, text, creation_date, user_id, meeting_id " +
            "FROM comments WHERE meeting_id = ?";
    private static final String GET_COMMENT_BY_ID = "SELECT comment_id, text, creation_date, user_id, meeting_id " +
            "FROM comments WHERE comment_id = ?";
    private static final String UPDATE_COMMENT = "UPDATE comments " +
            "SET text = ?, creation_date = ?, user_id = ?, meeting_id = ? " +
            "WHERE comment_id = ?";
    private static final String DELETE_COMMENT = "DELETE FROM comments WHERE comment_id = ?";

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;

    public CommentRepository(BaseConnectionManager connectionManager, UserRepository userRepository, MeetingRepository meetingRepository) {
        this.connectionManager = connectionManager;
        this.userRepository = userRepository;
        this.meetingRepository = meetingRepository;
    }

    @Override
    public Comment create(Comment comment) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_COMMENT, Statement.RETURN_GENERATED_KEYS)) {

            setStatement(comment, preparedStatement);

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                comment.setCommentId(generatedKeys.getLong(1));
            }

            return comment;
        } catch (SQLException e) {
            logger.error("Failed to create comment", e);
            throw new RuntimeException("Failed to create comment", e);
        }
    }

    @Override
    public List<Comment> getAll() {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_COMMENTS)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                comments.add(mapResultSetToComment(resultSet));
            }

        } catch (SQLException e) {
            logger.error("Failed to get all comments", e);
            throw new RuntimeException("Failed to get all comments", e);
        }
        return comments;
    }

    public List<Comment> getAllByMeetingId(Long meetingId) {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BY_MEETING_ID)) {

            preparedStatement.setLong(1, meetingId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                comments.add(mapResultSetToComment(resultSet));
            }

        } catch (SQLException e) {
            logger.error("Failed to get all comments", e);
            throw new RuntimeException("Failed to get all comments", e);
        }
        return comments;
    }

    @Override
    public Optional<Comment> getById(Long id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_COMMENT_BY_ID)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapResultSetToComment(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Failed to get comment by id", e);
            throw new RuntimeException("Failed to get comment by id", e);
        }
    }

    @Override
    public boolean update(Comment comment) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_COMMENT)) {

            setStatement(comment, preparedStatement);
            preparedStatement.setLong(5, comment.getCommentId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Failed to update comment", e);
            throw new RuntimeException("Failed to update comment", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_COMMENT)) {

            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Failed to delete comment", e);
            throw new RuntimeException("Failed to delete comment", e);
        }
    }

    private void setStatement(Comment comment, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, comment.getText());
        preparedStatement.setTimestamp(2, Timestamp.valueOf(comment.getDateTime()));
        preparedStatement.setLong(3, comment.getUser().getUserId());
        preparedStatement.setLong(4, comment.getMeeting().getMeetingId());
    }

    private Comment mapResultSetToComment(ResultSet resultSet) throws SQLException {
        User user = userRepository.getById(resultSet.getLong("user_id")).orElseThrow();
        Meeting meeting = meetingRepository.getById(resultSet.getLong("meeting_id")).orElseThrow();

        return Comment.builder()
                .commentId(resultSet.getLong("comment_id"))
                .text(resultSet.getString("text"))
                .dateTime(resultSet.getTimestamp("creation_date").toLocalDateTime())
                .user(user)
                .meeting(meeting)
                .build();
    }
}
