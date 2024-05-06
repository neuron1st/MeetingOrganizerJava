package services;

import dto.participant.CreateParticipantModel;
import dto.participant.ParticipantModel;
import mappers.participant.CreateParticipantMapper;
import mappers.participant.ParticipantMapper;
import repositories.ParticipantRepository;

import java.util.List;
import java.util.Optional;

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

    public List<ParticipantModel> getAllByMeetingId(long meetingId) {
        return participantRepository.getAllByMeetingId(meetingId)
                .stream()
                .map(participantMapper::map)
                .toList();
    }

    public List<ParticipantModel> getAllByUserId(long userId) {
        return participantRepository.getAllByMeetingId(userId)
                .stream()
                .map(participantMapper::map)
                .toList();
    }

    public Optional<ParticipantModel> getById(long meetingId, long userId) {
        ParticipantModel participantModel =  participantRepository.getById(meetingId, userId)
                .map(participantMapper::map)
                .orElse(null);
        if (participantModel == null) {
            return Optional.empty();
        }
        return Optional.of(participantModel);
    }


    public ParticipantModel create(CreateParticipantModel createModel) {
        return participantMapper.map(participantRepository.create(createParticipantMapper.map(createModel)));
    }

    public boolean delete(long meetingId, long userId) {
        return participantRepository.delete(meetingId, userId);
    }
}
