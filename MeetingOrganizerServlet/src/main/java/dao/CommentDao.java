package dao;

import entity.Comment;
import entity.Meeting;
import entity.User;
import utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentDao implements Dao<Long, Comment> {
    private static final String CREATE_COMMENT = "INSERT INTO comments (text, creation_date, user_id, meeting_id) " +
            "VALUES (?, ?, ?, ?)";
    private static final String GET_ALL_COMMENTS = "SELECT * FROM comments";
    private static final String GET_COMMENT_BY_ID = "SELECT * FROM comments WHERE comment_id = ?";
    private static final String UPDATE_COMMENT = "UPDATE comments " +
            "SET text = ?, creation_date = ?, user_id = ?, meeting_id = ? " +
            "WHERE comment_id = ?";
    private static final String DELETE_COMMENT = "DELETE FROM comments WHERE comment_id = ?";
    private final UserDao userDao;
    private final MeetingDao meetingDao;

    public CommentDao(UserDao userDao, MeetingDao meetingDao) {
        this.userDao = userDao;
        this.meetingDao = meetingDao;
    }

    @Override
    public Comment create(Comment comment) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_COMMENT, Statement.RETURN_GENERATED_KEYS)) {

            setStatement(comment, preparedStatement);

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                comment.setCommentId(generatedKeys.getLong(1));
            }

            return comment;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create comment", e);
        }
    }

    @Override
    public List<Comment> getAll() {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_COMMENTS)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                comments.add(mapResultSetToComment(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all comments", e);
        }
        return comments;
    }

    @Override
    public Optional<Comment> getById(Long id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_COMMENT_BY_ID)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                return Optional.of(mapResultSetToComment(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get comment by id", e);
        }
    }

    @Override
    public boolean update(Comment comment) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_COMMENT)) {

            setStatement(comment, preparedStatement);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update comment", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_COMMENT)) {

            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
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
        User user = userDao.getById(resultSet.getLong("user_id")).orElseThrow();
        Meeting meeting = meetingDao.getById(resultSet.getLong("meeting_id")).orElseThrow();

        return Comment.builder()
                .commentId(resultSet.getLong("comment_id"))
                .text(resultSet.getString("text"))
                .dateTime(resultSet.getTimestamp("creation_date").toLocalDateTime())
                .user(user)
                .meeting(meeting)
                .build();
    }
}