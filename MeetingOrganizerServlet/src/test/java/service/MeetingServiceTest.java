package service;

import dto.meeting.CreateMeetingModel;
import dto.meeting.MeetingModel;
import entity.Meeting;
import mappers.meeting.CreateMeetingMapper;
import mappers.meeting.MeetingMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repositories.CommentRepository;
import repositories.MeetingLikeRepository;
import repositories.MeetingRepository;
import repositories.ParticipantRepository;
import services.MeetingService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
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
    @Mock
    private MeetingMapper meetingMapper;
    @Mock
    private CreateMeetingMapper createMeetingMapper;

    @InjectMocks
    private MeetingService meetingService;

    private static MeetingModel getMeetingModel() {
        return MeetingModel.builder()
                .meetingId(1)
                .title("Test")
                .description("Test desc")
                .date(LocalDateTime.now())
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
        List<Meeting> meetings = List.of(meeting);
        MeetingModel meetingModel = getMeetingModel();

        when(meetingRepository.getAll()).thenReturn(meetings);
        when(meetingMapper.map(any())).thenReturn(meetingModel);

        Assertions.assertEquals(meetingService.getAll().size(), 1);
        Assertions.assertEquals(meetingService.getAll().get(0).getMeetingId(), meetingModel.getMeetingId());
    }

    @Test
    public void testGetByTitle() {
        Meeting meeting = getMeeting();
        List<Meeting> meetings = List.of(meeting);

        MeetingModel meetingModel = getMeetingModel();
        List<MeetingModel> expectedModels = List.of(meetingModel);

        when(meetingRepository.getByTitle(meeting.getTitle())).thenReturn(meetings);
        when(meetingMapper.map(any())).thenReturn(meetingModel);

        List<MeetingModel> actualModels = meetingService.getByTitle(meeting.getTitle());

        Assertions.assertEquals(expectedModels.size(), actualModels.size());
        Assertions.assertEquals(expectedModels.get(0), actualModels.get(0));
    }

    @Test
    public void testGetById() {
        long meetingId = 1L;
        Meeting meeting = getMeeting();
        MeetingModel meetingModel = getMeetingModel();

        when(meetingRepository.getById(meetingId)).thenReturn(Optional.of(meeting));
        when(meetingLikeRepository.getLikesCount(anyLong())).thenReturn(0);
        when(commentRepository.getAllByMeetingId(anyLong())).thenReturn(new ArrayList<>());
        when(participantRepository.getAllByMeetingId(anyLong())).thenReturn(new ArrayList<>());
        when(meetingMapper.map(meeting)).thenReturn(meetingModel);

        Assertions.assertTrue(meetingService.getById(meetingId).isPresent());
        Assertions.assertEquals(meetingService.getById(meetingId).get().getMeetingId(), meetingModel.getMeetingId());
    }

    @Test
    public void testCreate() {
        CreateMeetingModel createModel = CreateMeetingModel.builder()
                .title("Test")
                .description("Test desc")
                .build();
        Meeting meeting = getMeeting();
        MeetingModel meetingModel = getMeetingModel();

        when(createMeetingMapper.map(createModel)).thenReturn(meeting);
        when(meetingRepository.create(meeting)).thenReturn(meeting);
        when(meetingMapper.map(meeting)).thenReturn(meetingModel);

        Assertions.assertEquals(meetingService.create(createModel).getMeetingId(), meetingModel.getMeetingId());
    }

    @Test
    public void testUpdate() {
        CreateMeetingModel createModel = CreateMeetingModel.builder()
                .title("Test")
                .description("Test desc")
                .build();
        Meeting meeting = getMeeting();
        MeetingModel meetingModel = getMeetingModel();

        when(createMeetingMapper.map(createModel)).thenReturn(meeting);
        when(meetingRepository.update(meeting)).thenReturn(true);

        Assertions.assertTrue(meetingService.update(createModel));
    }

    @Test
    public void testDelete() {
        Long meetingId = 1L;
        when(meetingRepository.delete(meetingId)).thenReturn(true);

        Assertions.assertTrue(meetingService.delete(meetingId));
    }

    @Test
    public void testGetAllWhenNoMeetings() {
        when(meetingRepository.getAll()).thenReturn(Collections.emptyList());

        Assertions.assertTrue(meetingService.getAll().isEmpty());
    }

    @Test
    public void testGetByIdWhenMeetingNotFound() {
        long nonExistentMeetingId = 999L;
        when(meetingRepository.getById(nonExistentMeetingId)).thenReturn(Optional.empty());

        Assertions.assertFalse(meetingService.getById(nonExistentMeetingId).isPresent());
    }
}
