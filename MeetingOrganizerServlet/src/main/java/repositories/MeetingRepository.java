package repositories;

import entity.Meeting;
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

public class MeetingRepository implements Repository<Long, Meeting> {
    private static final String CREATE_MEETING = "INSERT INTO meetings (title, description, event_date) " +
            "VALUES (?, ?, ?)";
    private static final String GET_ALL_MEETINGS = "SELECT meeting_id, title, description, event_date " +
            "FROM meetings";
    private static final String GET_MEETING_BY_ID = "SELECT meeting_id, title, description, event_date " +
            "FROM meetings WHERE meeting_id = ?";
    private static final String UPDATE_MEETING = "UPDATE meetings " +
            "SET title = ?, description = ?, event_date = ? " +
            "WHERE meeting_id = ?";
    private static final String DELETE_MEETING = "DELETE FROM meetings WHERE meeting_id = ?";
    private static final String GET_BY_TITLE = "SELECT meeting_id, title, description, event_date " +
            "FROM meetings WHERE title LIKE ?";

    @Override
    public Meeting create(Meeting meeting) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_MEETING, Statement.RETURN_GENERATED_KEYS)) {

            setStatement(meeting, preparedStatement);

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                meeting.setMeetingId(generatedKeys.getLong(1));
            }

            return meeting;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create meeting", e);
        }
    }

    @Override
    public List<Meeting> getAll() {
        List<Meeting> meetings = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_MEETINGS, Statement.RETURN_GENERATED_KEYS)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                meetings.add(mapResultSetToMeeting(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all meetings", e);
        }
        return meetings;
    }

    public List<Meeting> getByTitle(String title) {
        List<Meeting> meetings = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_TITLE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, "%" + title + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                meetings.add(mapResultSetToMeeting(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get meetings", e);
        }
        return meetings;
    }

    @Override
    public Optional<Meeting> getById(Long id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_MEETING_BY_ID, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(mapResultSetToMeeting(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get meeting by id", e);
        }
    }

    @Override
    public boolean update(Meeting meeting) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_MEETING)) {

            setStatement(meeting, preparedStatement);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update meeting", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_MEETING)) {

            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete meeting", e);
        }
    }

    private void setStatement(Meeting meeting, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, meeting.getTitle());
        preparedStatement.setString(2, meeting.getDescription());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(meeting.getDate()));
    }

    private Meeting mapResultSetToMeeting(ResultSet resultSet) throws SQLException {
        return Meeting.builder()
                .meetingId(resultSet.getLong("meeting_id"))
                .title(resultSet.getString("title"))
                .description(resultSet.getString("description"))
                .date(resultSet.getTimestamp("event_date").toLocalDateTime())
                .build();
    }
}
