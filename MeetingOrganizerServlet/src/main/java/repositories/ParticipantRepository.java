package repositories;

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

public class ParticipantRepository implements Repository<Long, Participant> {
    private static final String ADD_PARTICIPANT = "INSERT INTO participants (meeting_id, user_id, role) " +
            "VALUES (?, ?, ?)";
    private static final String GET_ALL_PARTICIPANTS = "SELECT meeting_id, user_id, role " +
            "FROM participants";
    private static final String GET_ALL_BY_MEETING_ID = "SELECT meeting_id, user_id, role " +
            "FROM participants WHERE meeting_id = ?";
    private static final String GET_ALL_BY_USER_ID = "SELECT meeting_id, user_id, role " +
            "FROM participants WHERE user_id = ?";
    private static final String GET_PARTICIPANT_BY_ID = "SELECT meeting_id, user_id, role " +
            "FROM participants WHERE meeting_id = ? AND user_id = ?";
    private static final String UPDATE_PARTICIPANT = "UPDATE participants " +
            "SET role = ? " +
            "WHERE meeting_id = ? AND user_id = ?";
    private static final String DELETE_PARTICIPANT = "DELETE FROM participants WHERE meeting_id = ? AND user_id = ?";
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;

    public ParticipantRepository(UserRepository userRepository, MeetingRepository meetingRepository) {
        this.userRepository = userRepository;
        this.meetingRepository = meetingRepository;
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
            while (resultSet.next()) {
                participants.add(mapResultSetToParticipant(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all participants", e);
        }
        return participants;
    }

    public List<Participant> getAllByMeetingId(Long meetingId) {
        List<Participant> participants = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BY_MEETING_ID)) {

            preparedStatement.setLong(1, meetingId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                participants.add(mapResultSetToParticipant(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get participants", e);
        }
        return participants;
    }

    public List<Participant> getAllByUserId(Long userId) {
        List<Participant> participants = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BY_USER_ID)) {

            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                participants.add(mapResultSetToParticipant(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get participants", e);
        }
        return participants;
    }

    @Override
    public Optional<Participant> getById(Long id) {
        throw new UnsupportedOperationException("Getting a participant by ID is not supported. " +
                "Use the composite key (meeting_id, user_id).");
    }

    public Optional<Participant> getById(Long meetingId, Long userId) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_PARTICIPANT_BY_ID)) {

            preparedStatement.setLong(1, meetingId);
            preparedStatement.setLong(2, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToParticipant(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get participant by id", e);
        }
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
        throw new UnsupportedOperationException("Deleting a participant by ID is not supported. " +
                "Use the composite key (meeting_id, user_id).");
    }

    public boolean delete(Long meetingId, Long userId) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PARTICIPANT)) {

            preparedStatement.setLong(1, meetingId);
            preparedStatement.setLong(2, userId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete participant", e);
        }
    }


    private void setStatement(Participant participant, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, participant.getMeeting().getMeetingId());
        preparedStatement.setLong(2, participant.getUser().getUserId());
        preparedStatement.setString(3, participant.getRole().name());
    }

    private Participant mapResultSetToParticipant(ResultSet resultSet) throws SQLException {
        User user = userRepository.getById(resultSet.getLong("user_id")).orElseThrow();
        Meeting meeting = meetingRepository.getById(resultSet.getLong("meeting_id")).orElseThrow();
        Role role = Role.valueOf(resultSet.getString("role"));

        return Participant.builder()
                .participantId(resultSet.getLong("user_id"))
                .user(user)
                .meeting(meeting)
                .role(role)
                .build();
    }
}
