package service;

import dto.participant.CreateParticipantModel;
import dto.participant.ParticipantModel;
import entity.Meeting;
import entity.Participant;
import entity.Role;
import entity.User;
import mappers.participant.CreateParticipantMapper;
import mappers.participant.ParticipantMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import repositories.ParticipantRepository;
import services.ParticipantService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;
    @Spy
    private ParticipantMapper participantMapper = new ParticipantMapper();
    @Spy
    private CreateParticipantMapper createParticipantMapper = new CreateParticipantMapper();

    @InjectMocks
    private ParticipantService participantService;


    private static Meeting getMeeting() {
        return Meeting.builder()
                .meetingId(1L)
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

    private static Participant getParticipant() {
        return Participant.builder()
                .meeting(getMeeting())
                .user(getUser())
                .role(Role.Participant)
                .build();
    }

    private static ParticipantModel getParticipantModel() {
        return ParticipantModel.builder()
                .meetingName(getMeeting().getTitle())
                .userName(getUser().getFullName())
                .role(Role.Participant)
                .build();
    }

    @Test
    public void testGetAll() {
        Participant participant = getParticipant();
        ParticipantModel participantModel = getParticipantModel();

        when(participantRepository.getAll()).thenReturn(List.of(participant));

        List<ParticipantModel> participantModels = participantService.getAll();

        assertEquals(1, participantModels.size());
        assertParticipantsEquals(participantModel, participantModels.get(0));
    }

    @Test
    public void testGetAllByMeetingId() {
        Participant participant = getParticipant();
        ParticipantModel participantModel = getParticipantModel();
        long meetingId = participant.getMeeting().getMeetingId();

        when(participantRepository.getAllByMeetingId(meetingId)).thenReturn(List.of(participant));

        List<ParticipantModel> participantModels = participantService.getAllByMeetingId(meetingId);

        assertEquals(1, participantModels.size());
        assertParticipantsEquals(participantModel, participantModels.get(0));    }

    @Test
    public void testCreate() {
        Participant participant = getParticipant();
        ParticipantModel participantModel = getParticipantModel();
        CreateParticipantModel createModel = CreateParticipantModel.builder()
                .userId(participant.getUser().getUserId())
                .meetingId(participant.getMeeting().getMeetingId())
                .role(participant.getRole())
                .build();

        when(participantRepository.create(any())).thenReturn(participant);

        assertParticipantsEquals(participantModel, participantService.create(createModel));
    }

    @Test
    public void testDelete() {
        long meetingId = 1L;
        long userId = 1L;
        when(participantRepository.delete(meetingId, userId)).thenReturn(true);

        assertTrue(participantService.delete(meetingId, userId));
    }

    @Test
    public void testGetAllWhenNoParticipants() {
        when(participantRepository.getAll()).thenReturn(Collections.emptyList());

        assertTrue(participantService.getAll().isEmpty());
    }

    @Test
    public void testGetAllByMeetingIdWhenNoParticipants() {
        long meetingId = 1L;
        when(participantRepository.getAllByMeetingId(meetingId)).thenReturn(Collections.emptyList());

        assertTrue(participantService.getAllByMeetingId(meetingId).isEmpty());
    }

    @Test
    public void testGetAllByUserIdWhenNoParticipants() {
        long userId = 1L;
        when(participantRepository.getAllByMeetingId(userId)).thenReturn(Collections.emptyList());

        assertTrue(participantService.getAllByUserId(userId).isEmpty());
    }

    @Test
    public void testDeleteNonExistentParticipant() {
        long nonExistentMeetingId = 999L;
        long nonExistentUserId = 999L;

        when(participantRepository.delete(nonExistentMeetingId, nonExistentUserId)).thenReturn(false);

        assertFalse(participantService.delete(nonExistentMeetingId, nonExistentUserId));
    }

    private void assertParticipantsEquals(ParticipantModel expected, ParticipantModel actual) {
        assertEquals(expected.getUserName(), actual.getUserName());
        assertEquals(expected.getMeetingName(), actual.getMeetingName());
        assertEquals(expected.getRole(), actual.getRole());
    }
}
