package dao;

import entity.Meeting;
import entity.Participant;
import entity.Role;
import entity.User;
import utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParticipantDao implements Dao<Long, Participant> {
    private static final String ADD_PARTICIPANT = "INSERT INTO participants (meeting_id, user_id, role) " +
            "VALUES (?, ?, ?)";
    private static final String GET_ALL_PARTICIPANTS = "SELECT * FROM participants";
    private static final String GET_PARTICIPANT_BY_ID = "SELECT * FROM participants WHERE meeting_id = ? AND user_id = ?";
    private static final String UPDATE_PARTICIPANT = "UPDATE participants " +
            "SET role = ? " +
            "WHERE meeting_id = ? AND user_id = ?";
    private static final String DELETE_PARTICIPANT = "DELETE FROM participants WHERE meeting_id = ? AND user_id = ?";
    private final UserDao userDao;
    private final MeetingDao meetingDao;

    public ParticipantDao(UserDao userDao, MeetingDao meetingDao) {
        this.userDao = userDao;
        this.meetingDao = meetingDao;
    }

    @Override
    public Participant create(Participant participant) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_PARTICIPANT)) {

            setStatement(participant, preparedStatement);

            preparedStatement.executeUpdate();

            return participant;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add participant", e);
        }
    }

    @Override
    public List<Participant> getAll() {
        List<Participant> participants = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_PARTICIPANTS)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                participants.add(mapResultSetToParticipant(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all participants", e);
        }
        return participants;
    }

    @Override
    public Optional<Participant> getById(Long id) {
        throw new UnsupportedOperationException("Getting a participant by ID is not supported. Use the composite key (meeting_id, user_id).");
    }

    @Override
    public boolean update(Participant participant) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PARTICIPANT)) {

            setStatement(participant, preparedStatement);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update participant", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        throw new UnsupportedOperationException("Deleting a participant by ID is not supported. Use the composite key (meeting_id, user_id).");
    }

    private void setStatement(Participant participant, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, participant.getMeeting().getMeetingId());
        preparedStatement.setLong(2, participant.getUser().getUserId());
        preparedStatement.setString(3, participant.getRole().name());
    }

    private Participant mapResultSetToParticipant(ResultSet resultSet) throws SQLException {
        User user = userDao.getById(resultSet.getLong("user_id")).orElseThrow();
        Meeting meeting = meetingDao.getById(resultSet.getLong("meeting_id")).orElseThrow();
        Role role = Role.valueOf(resultSet.getString("role"));

        return Participant.builder()
                .participantId(resultSet.getLong("user_id"))
                .user(user)
                .meeting(meeting)
                .role(role)
                .build();
    }
}
