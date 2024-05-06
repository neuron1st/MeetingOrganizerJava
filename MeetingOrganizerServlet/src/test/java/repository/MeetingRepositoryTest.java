package repository;

import entity.Meeting;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repositories.MeetingRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MeetingRepositoryTest {
    private final TestConnectionManager connectionManager = new TestConnectionManager();
    private final MeetingRepository meetingRepository = new MeetingRepository(connectionManager);

    private Meeting createSampleMeeting() {
        return Meeting.builder()
                .title("Sample Meeting")
                .description("This is a sample meeting")
                .date(LocalDateTime.now())
                .build();
    }


    @BeforeEach
    @AfterEach
    public void clear() {
        try (Connection connection = connectionManager.getConnection()) {
            String CLEAR_TABLE = "TRUNCATE meetings RESTART IDENTITY CASCADE";
            connection.prepareStatement(CLEAR_TABLE).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Create Meeting - Success")
    void testCreateMeeting_Success() {
        Meeting meeting = createSampleMeeting();
        Meeting createdMeeting = meetingRepository.create(meeting);

        assertEquals(meeting.getTitle(), createdMeeting.getTitle());
        assertEquals(meeting.getDescription(), createdMeeting.getDescription());
        assertEquals(meeting.getDate(), createdMeeting.getDate());
    }

    @Test
    @DisplayName("Get Meeting By Id - Success")
    void testGetMeetingById_Success() {
        Meeting meeting = createSampleMeeting();
        Meeting createdMeeting = meetingRepository.create(meeting);

        Optional<Meeting> retrievedMeetingOpt = meetingRepository.getById(createdMeeting.getMeetingId());
        assertTrue(retrievedMeetingOpt.isPresent());

        Meeting retrievedMeeting = retrievedMeetingOpt.get();
        assertMeetingsEqual(createdMeeting, retrievedMeeting);
    }

    @Test
    @DisplayName("Get Meeting By Id - Not Found")
    void testGetMeetingById_NotFound() {
        Optional<Meeting> retrievedMeetingOpt = meetingRepository.getById(-1L);
        assertFalse(retrievedMeetingOpt.isPresent());
    }

    @Test
    @DisplayName("Update Meeting - Success")
    void testUpdateMeeting_Success() {
        Meeting meeting = createSampleMeeting();
        Meeting createdMeeting = meetingRepository.create(meeting);

        createdMeeting.setTitle("Updated Title");
        createdMeeting.setDescription("Updated Description");
        createdMeeting.setDate(LocalDateTime.now().plusDays(1));

        assertTrue(meetingRepository.update(createdMeeting));
        Optional<Meeting> updatedMeetingOpt = meetingRepository.getById(createdMeeting.getMeetingId());
        assertTrue(updatedMeetingOpt.isPresent());

        Meeting updatedMeeting = updatedMeetingOpt.get();
        assertMeetingsEqual(createdMeeting, updatedMeeting);
    }

    @Test
    @DisplayName("Delete Meeting - Success")
    void testDeleteMeeting_Success() {
        Meeting meeting = createSampleMeeting();
        Meeting createdMeeting = meetingRepository.create(meeting);

        assertTrue(meetingRepository.delete(createdMeeting.getMeetingId()));
        Optional<Meeting> deletedMeetingOpt = meetingRepository.getById(createdMeeting.getMeetingId());
        assertFalse(deletedMeetingOpt.isPresent());
    }

    @Test
    @DisplayName("Get All Meetings - Success")
    void testGetAllMeetings_Success() {
        Meeting meeting1 = createSampleMeeting();
        Meeting meeting2 = createSampleMeeting();

        meetingRepository.create(meeting1);
        meetingRepository.create(meeting2);

        List<Meeting> meetings = meetingRepository.getAll();
        assertEquals(2, meetings.size());
    }

    @Test
    @DisplayName("Get Meetings By Title - Success")
    void testGetMeetingsByTitle_Success() {
        Meeting meeting1 = createSampleMeeting();
        Meeting meeting2 = createSampleMeeting();
        meeting2.setTitle("Meeting 2");

        meetingRepository.create(meeting1);
        meetingRepository.create(meeting2);

        List<Meeting> meetings = meetingRepository.getByTitle("Meeting");
        assertEquals(2, meetings.size());

        meetings = meetingRepository.getByTitle("2");
        assertEquals(1, meetings.size());
        assertMeetingsEqual(meeting2, meetings.get(0));
    }

    @Test
    @DisplayName("Update Meeting - Not Found")
    void testUpdateMeeting_NotFound() {
        Meeting meeting = createSampleMeeting();
        meeting.setMeetingId(-1L);

        assertFalse(meetingRepository.update(meeting));
    }

    @Test
    @DisplayName("Delete Meeting - Not Found")
    void testDeleteMeeting_NotFound() {
        assertFalse(meetingRepository.delete(-1L));
    }

    private static void assertMeetingsEqual(Meeting expected, Meeting actual) {
        assertEquals(expected.getMeetingId(), actual.getMeetingId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getDate().truncatedTo(ChronoUnit.SECONDS), actual.getDate().truncatedTo(ChronoUnit.SECONDS));
    }
}
