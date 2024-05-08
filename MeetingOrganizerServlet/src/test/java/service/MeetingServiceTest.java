package service;

import dto.meeting.CreateMeetingModel;
import dto.meeting.MeetingModel;
import entity.Meeting;
import mappers.meeting.CreateMeetingMapper;
import mappers.meeting.MeetingMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import repositories.CommentRepository;
import repositories.MeetingLikeRepository;
import repositories.MeetingRepository;
import repositories.ParticipantRepository;
import services.MeetingService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MeetingServiceTest {

    @Mock
    private MeetingRepository meetingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private MeetingLikeRepository meetingLikeRepository;
    @Spy
    private MeetingMapper meetingMapper = new MeetingMapper();
    @Spy
    private CreateMeetingMapper createMeetingMapper = new CreateMeetingMapper();

    @InjectMocks
    private MeetingService meetingService;

    private static MeetingModel getMeetingModel(Meeting meeting) {
        return MeetingModel.builder()
                .meetingId(meeting.getMeetingId())
                .title(meeting.getTitle())
                .description(meeting.getDescription())
                .date(meeting.getDate())
                .commentCount(0)
                .likeCount(0)
                .participantCount(0)
                .build();
    }

    private static Meeting getMeeting() {
        return Meeting.builder()
                .meetingId(1)
                .title("Test")
                .description("Test desc")
                .date(LocalDateTime.now())
                .build();
    }

    @Test
    public void testGetAll() {
        Meeting meeting = getMeeting();
        MeetingModel meetingModel = getMeetingModel(meeting);

        when(meetingRepository.getAll()).thenReturn(List.of(meeting));
        when(meetingLikeRepository.getLikesCount(anyLong())).thenReturn(0);
        when(commentRepository.getAllByMeetingId(anyLong())).thenReturn(Collections.emptyList());
        when(participantRepository.getAllByMeetingId(anyLong())).thenReturn(Collections.emptyList());

        List<MeetingModel> actualModels = meetingService.getAll();

        assertEquals(1, actualModels.size());
        assertMeetingEquals(meetingModel, actualModels.get(0));
    }

    @Test
    public void testGetByTitle() {
        Meeting meeting = getMeeting();
        MeetingModel meetingModel = getMeetingModel(meeting);

        when(meetingRepository.getByTitle(any())).thenReturn(List.of(meeting));
        when(meetingLikeRepository.getLikesCount(anyLong())).thenReturn(0);
        when(commentRepository.getAllByMeetingId(anyLong())).thenReturn(Collections.emptyList());
        when(participantRepository.getAllByMeetingId(anyLong())).thenReturn(Collections.emptyList());

        List<MeetingModel> actualModels = meetingService.getByTitle(meeting.getTitle());

        assertEquals(1, actualModels.size());
        assertMeetingEquals(meetingModel, actualModels.get(0));
    }

    @Test
    public void testGetById() {
        Meeting meeting = getMeeting();
        MeetingModel meetingModel = getMeetingModel(meeting);
        long meetingId = meeting.getMeetingId();

        when(meetingRepository.getById(any())).thenReturn(Optional.of(meeting));
        when(meetingLikeRepository.getLikesCount(anyLong())).thenReturn(0);
        when(commentRepository.getAllByMeetingId(anyLong())).thenReturn(Collections.emptyList());
        when(participantRepository.getAllByMeetingId(anyLong())).thenReturn(Collections.emptyList());

        Optional<MeetingModel> actual = meetingService.getById(meetingId);

        assertTrue(actual.isPresent());
        assertMeetingEquals(meetingModel, actual.get());
    }

    @Test
    public void testCreate() {
        Meeting meeting = getMeeting();
        MeetingModel meetingModel = getMeetingModel(meeting);
        CreateMeetingModel createModel = CreateMeetingModel.builder()
                .title(meeting.getTitle())
                .description(meeting.getDescription())
                .build();

        when(meetingRepository.create(any())).thenReturn(meeting);

        MeetingModel actual = meetingService.create(createModel);

        assertMeetingEquals(meetingModel, actual);
    }

    @Test
    public void testUpdate() {
        Meeting meeting = getMeeting();
        CreateMeetingModel createModel = CreateMeetingModel.builder()
                .title("Test")
                .description("Test desc")
                .date(LocalDateTime.now())
                .build();

        when(meetingRepository.getById(meeting.getMeetingId())).thenReturn(Optional.of(meeting));
        when(meetingRepository.update(meeting)).thenReturn(true);

        assertTrue(meetingService.update(meeting.getMeetingId(), createModel));
    }

    @Test
    public void testDelete() {
        long meetingId = 1L;
        when(meetingRepository.delete(meetingId)).thenReturn(true);

        assertTrue(meetingService.delete(meetingId));
    }

    @Test
    public void testGetAllWhenNoMeetings() {
        when(meetingRepository.getAll()).thenReturn(Collections.emptyList());

        assertTrue(meetingService.getAll().isEmpty());
    }

    @Test
    public void testGetByIdWhenMeetingNotFound() {
        long nonExistentMeetingId = 999L;
        when(meetingRepository.getById(nonExistentMeetingId)).thenReturn(Optional.empty());

        assertFalse(meetingService.getById(nonExistentMeetingId).isPresent());
    }

    private void assertMeetingEquals(MeetingModel expected, MeetingModel actual) {
        assertEquals(expected.getMeetingId(), actual.getMeetingId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getCommentCount(), actual.getCommentCount());
        assertEquals(expected.getLikeCount(), actual.getLikeCount());
        assertEquals(expected.getParticipantCount(), actual.getParticipantCount());
    }
}
