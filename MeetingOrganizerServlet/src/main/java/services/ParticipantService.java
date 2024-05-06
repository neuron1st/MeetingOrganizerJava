package services;

import dto.participant.CreateParticipantModel;
import dto.participant.ParticipantModel;
import mappers.participant.CreateParticipantMapper;
import mappers.participant.ParticipantMapper;
import repositories.MeetingRepository;
import repositories.ParticipantRepository;
import repositories.UserRepository;
import utils.BaseConnectionManager;
import utils.ConnectionManager;

import java.util.List;

public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;
    private final CreateParticipantMapper createParticipantMapper;

    public ParticipantService(
            ParticipantRepository participantRepository,
            ParticipantMapper participantMapper,
            CreateParticipantMapper createParticipantMapper
    ) {
        this.participantRepository = participantRepository;
        this.participantMapper = participantMapper;
        this.createParticipantMapper = createParticipantMapper;
    }

    public List<ParticipantModel> getAll() {
        return participantRepository.getAll()
                .stream()
                .map(participantMapper::map)
                .toList();
    }

    public List<ParticipantModel> getAllByMeetingId(Long meetingId) {
        return participantRepository.getAllByMeetingId(meetingId)
                .stream()
                .map(participantMapper::map)
                .toList();
    }

    public List<ParticipantModel> getAllByUserId(Long userId) {
        return participantRepository.getAllByMeetingId(userId)
                .stream()
                .map(participantMapper::map)
                .toList();
    }

    public ParticipantModel create(CreateParticipantModel createModel) {
        return participantMapper.map(participantRepository.create(createParticipantMapper.map(createModel)));
    }

    public boolean delete(Long meetingId, Long userId) {
        return participantRepository.delete(meetingId, userId);
    }
}
