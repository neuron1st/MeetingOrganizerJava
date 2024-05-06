package repository;

import entity.Meeting;
import entity.Participant;
import entity.Role;
import entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repositories.MeetingRepository;
import repositories.ParticipantRepository;
import repositories.UserRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ParticipantRepositoryTest {
    private final TestConnectionManager connectionManager = new TestConnectionManager();
    private final UserRepository userRepository = new UserRepository(connectionManager);
    private final MeetingRepository meetingRepository = new MeetingRepository(connectionManager);
    private final ParticipantRepository participantRepository = new ParticipantRepository(connectionManager, userRepository, meetingRepository);

    private User createUser() {
        return User.builder()
                .fullName("Test User")
                .email("test@example.com")
                .password("password")
                .build();
    }

    private Meeting createMeeting() {
        return Meeting.builder()
                .title("Test Meeting")
                .description("This is a test meeting")
                .date(LocalDateTime.now())
                .build();
    }

    @BeforeEach
    @AfterEach
    public void clear() {
        try (Connection connection = connectionManager.getConnection()) {
            String CLEAR_PARTICIPANTS_TABLE = "TRUNCATE participants RESTART IDENTITY CASCADE";
            connection.prepareStatement(CLEAR_PARTICIPANTS_TABLE).executeUpdate();

            String CLEAR_USERS_TABLE = "TRUNCATE users RESTART IDENTITY CASCADE";
            connection.prepareStatement(CLEAR_USERS_TABLE).executeUpdate();

            String CLEAR_MEETINGS_TABLE = "TRUNCATE meetings RESTART IDENTITY CASCADE";
            connection.prepareStatement(CLEAR_MEETINGS_TABLE).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Create Participant - Success")
    void testCreateParticipant_Success() {
        User user = createUser();
        user = userRepository.create(user);

        Meeting meeting = createMeeting();
        meeting = meetingRepository.create(meeting);

        Participant participant = Participant.builder()
                .user(user)
                .meeting(meeting)
                .role(Role.Participant)
                .build();

        Participant createdParticipant = participantRepository.create(participant);

        assertEquals(participant.getUser().getUserId(), createdParticipant.getUser().getUserId());
        assertEquals(participant.getMeeting().getMeetingId(), createdParticipant.getMeeting().getMeetingId());
        assertEquals(participant.getRole(), createdParticipant.getRole());
    }

    @Test
    @DisplayName("Get All Participants - Success")
    void testGetAllParticipants_Success() {
        User user = createUser();
        user = userRepository.create(user);

        Meeting meeting = createMeeting();
        meeting = meetingRepository.create(meeting);

        Participant participant = Participant.builder()
                .user(user)
                .meeting(meeting)
                .role(Role.Participant)
                .build();

        participantRepository.create(participant);

        List<Participant> participants = participantRepository.getAll();
        assertEquals(1, participants.size());
    }

    @Test
    @DisplayName("Get All Participants By Meeting Id - Success")
    void testGetAllParticipantsByMeetingId_Success() {
        User user = createUser();
        user = userRepository.create(user);

        Meeting meeting = createMeeting();
        meeting = meetingRepository.create(meeting);

        Participant participant = Participant.builder()
                .user(user)
                .meeting(meeting)
                .role(Role.Participant)
                .build();

        participantRepository.create(participant);

        List<Participant> participants = participantRepository.getAllByMeetingId(meeting.getMeetingId());
        assertEquals(1, participants.size());
    }

    @Test
    @DisplayName("Get All Participants By User Id - Success")
    void testGetAllParticipantsByUserId_Success() {
        User user = createUser();
        user = userRepository.create(user);

        Meeting meeting = createMeeting();
        meeting = meetingRepository.create(meeting);

        Participant participant = Participant.builder()
                .user(user)
                .meeting(meeting)
                .role(Role.Participant)
                .build();

        participantRepository.create(participant);

        List<Participant> participants = participantRepository.getAllByUserId(user.getUserId());
        assertEquals(1, participants.size());
    }

    @Test
    @DisplayName("Get Participant By Id - Success")
    void testGetParticipantById_Success() {
        User user = createUser();
        user = userRepository.create(user);

        Meeting meeting = createMeeting();
        meeting = meetingRepository.create(meeting);

        Participant participant = Participant.builder()
                .user(user)
                .meeting(meeting)
                .role(Role.Participant)
                .build();

        participant = participantRepository.create(participant);

        Optional<Participant> retrievedParticipantOpt = participantRepository.getById(meeting.getMeetingId(), user.getUserId());
        assertTrue(retrievedParticipantOpt.isPresent());

        Participant retrievedParticipant = retrievedParticipantOpt.get();
        assertEquals(participant.getUser().getUserId(), retrievedParticipant.getUser().getUserId());
        assertEquals(participant.getMeeting().getMeetingId(), retrievedParticipant.getMeeting().getMeetingId());
        assertEquals(participant.getRole(), retrievedParticipant.getRole());
    }

    @Test
    @DisplayName("Update Participant - Success")
    void testUpdateParticipant_Success() {
        User user = createUser();
        user = userRepository.create(user);

        Meeting meeting = createMeeting();
        meeting = meetingRepository.create(meeting);

        Participant participant = Participant.builder()
                .user(user)
                .meeting(meeting)
                .role(Role.Participant)
                .build();

        participant = participantRepository.create(participant);

        participant.setRole(Role.Admin);

        assertTrue(participantRepository.update(participant));

        Optional<Participant> updatedParticipantOpt = participantRepository.getById(meeting.getMeetingId(), user.getUserId());
        assertTrue(updatedParticipantOpt.isPresent());

        Participant updatedParticipant = updatedParticipantOpt.get();
        assertEquals(participant.getUser().getUserId(), updatedParticipant.getUser().getUserId());
        assertEquals(participant.getMeeting().getMeetingId(), updatedParticipant.getMeeting().getMeetingId());
        assertEquals(participant.getRole(), updatedParticipant.getRole());
    }

    @Test
    @DisplayName("Delete Participant - Success")
    void testDeleteParticipant_Success() {
        User user = createUser();
        user = userRepository.create(user);

        Meeting meeting = createMeeting();
        meeting = meetingRepository.create(meeting);

        Participant participant = Participant.builder()
                .user(user)
                .meeting(meeting)
                .role(Role.Participant)
                .build();

        participantRepository.create(participant);

        assertTrue(participantRepository.delete(meeting.getMeetingId(), user.getUserId()));

        Optional<Participant> deletedParticipantOpt = participantRepository.getById(meeting.getMeetingId(), user.getUserId());
        assertFalse(deletedParticipantOpt.isPresent());
    }

    @Test
    @DisplayName("Create Participant - Invalid User")
    void testCreateParticipant_InvalidUser() {
        Meeting meeting = createMeeting();
        meeting = meetingRepository.create(meeting);

        Participant participant = Participant.builder()
                .user(User.builder().userId(-1L).build())
                .meeting(meeting)
                .role(Role.Participant)
                .build();

        assertThrows(RuntimeException.class, () -> participantRepository.create(participant));
    }

    @Test
    @DisplayName("Create Participant - Invalid Meeting")
    void testCreateParticipant_InvalidMeeting() {
        User user = createUser();
        user = userRepository.create(user);

        Participant participant = Participant.builder()
                .user(user)
                .meeting(Meeting.builder().meetingId(-1L).build())
                .role(Role.Participant)
                .build();

        assertThrows(RuntimeException.class, () -> participantRepository.create(participant));
    }

    @Test
    @DisplayName("Get Participant By Id - Not Found")
    void testGetParticipantById_NotFound() {
        Optional<Participant> participant = participantRepository.getById(999L, 999L);

        assertFalse(participant.isPresent());
    }

    @Test
    @DisplayName("Update Participant - Not Found")
    void testUpdateParticipant_NotFound() {
        User user = createUser();
        user = userRepository.create(user);

        Meeting meeting = createMeeting();
        meeting = meetingRepository.create(meeting);

        Participant participant = Participant.builder()
                .user(user)
                .meeting(meeting)
                .role(Role.Participant)
                .build();

        participantRepository.create(participant);

        participant.setRole(Role.Admin);
        participant.setUser(User.builder().userId(-1L).build());

        assertFalse(participantRepository.update(participant));
    }

    @Test
    @DisplayName("Delete Participant - Not Found")
    void testDeleteParticipant_NotFound() {
        User user = createUser();
        user = userRepository.create(user);

        Meeting meeting = createMeeting();
        meeting = meetingRepository.create(meeting);

        assertFalse(participantRepository.delete(meeting.getMeetingId(), user.getUserId()));
    }

}
