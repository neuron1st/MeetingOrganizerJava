package service;

import dto.meeting.MeetingModel;
import dto.participant.CreateParticipantModel;
import dto.participant.ParticipantModel;
import dto.user.UserModel;
import entity.Meeting;
import entity.Participant;
import entity.Role;
import entity.User;
import mappers.participant.CreateParticipantMapper;
import mappers.participant.ParticipantMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repositories.ParticipantRepository;
import services.ParticipantService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private ParticipantMapper participantMapper;
    @Mock
    private CreateParticipantMapper createParticipantMapper;

    @InjectMocks
    private ParticipantService participantService;


    private static Meeting getMeeting() {
        return Meeting.builder()
                .meetingId(1)
                .title("Test")
                .description("Test desc")
                .date(LocalDateTime.now())
                .build();
    }

    private static User getUser() {
        return User.builder()
                .userId(1L)
                .email("test@example.com")
                .password("hashedPassword")
                .build();
    }

    @Test
    public void testGetAll() {
        List<Participant> participants = new ArrayList<>();
        participants.add(Participant.builder().build());

        when(participantRepository.getAll()).thenReturn(participants);
        when(participantMapper.map(any())).thenReturn(ParticipantModel.builder().build());

        Assertions.assertFalse(participantService.getAll().isEmpty());
    }

    @Test
    public void testGetAllByMeetingId() {
        long meetingId = 1L;
        List<Participant> participants = new ArrayList<>();
        participants.add(Participant.builder().build());

        when(participantRepository.getAllByMeetingId(meetingId)).thenReturn(participants);
        when(participantMapper.map(any())).thenReturn(ParticipantModel.builder().build());

        Assertions.assertFalse(participantService.getAllByMeetingId(meetingId).isEmpty());
    }

    @Test
    public void testCreate() {
        CreateParticipantModel createModel = CreateParticipantModel.builder()
                .userId(1L)
                .meetingId(1L)
                .role(Role.Participant)
                .build();

        Participant participant = Participant.builder()
                .user(getUser())
                .meeting(getMeeting())
                .role(Role.Participant)
                .build();

        ParticipantModel participantModel = ParticipantModel.builder()
                .userName(getUser().getFullName())
                .meetingName(getMeeting().getTitle())
                .build();

        when(createParticipantMapper.map(createModel)).thenReturn(participant);
        when(participantRepository.create(any())).thenReturn(participant);
        when(participantMapper.map(any())).thenReturn(participantModel);

        Assertions.assertEquals(participantService.create(createModel), participantModel);
    }

    @Test
    public void testDelete() {
        long meetingId = 1L;
        long userId = 1L;
        when(participantRepository.delete(meetingId, userId)).thenReturn(true);

        Assertions.assertTrue(participantService.delete(meetingId, userId));
    }

    @Test
    public void testGetAllWhenNoParticipants() {
        when(participantRepository.getAll()).thenReturn(Collections.emptyList());

        Assertions.assertTrue(participantService.getAll().isEmpty());
    }

    @Test
    public void testGetAllByMeetingIdWhenNoParticipants() {
        long meetingId = 1L;
        when(participantRepository.getAllByMeetingId(meetingId)).thenReturn(Collections.emptyList());

        Assertions.assertTrue(participantService.getAllByMeetingId(meetingId).isEmpty());
    }

    @Test
    public void testGetAllByUserIdWhenNoParticipants() {
        long userId = 1L;
        when(participantRepository.getAllByMeetingId(userId)).thenReturn(Collections.emptyList());

        Assertions.assertTrue(participantService.getAllByUserId(userId).isEmpty());
    }

    @Test
    public void testDeleteNonExistentParticipant() {
        long nonExistentMeetingId = 999L;
        long nonExistentUserId = 999L;

        when(participantRepository.delete(nonExistentMeetingId, nonExistentUserId)).thenReturn(false);

        Assertions.assertFalse(participantService.delete(nonExistentMeetingId, nonExistentUserId));
    }
}
