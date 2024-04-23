package services;

import repositories.ParticipantRepository;
import dto.participant.CreateParticipantModel;
import dto.participant.ParticipantModel;
import mappers.participant.CreateParticipantMapper;
import mappers.participant.ParticipantMapper;
import utils.RepositoryManager;

import java.util.List;

public class ParticipantService {
    private final ParticipantRepository participantRepository = RepositoryManager.getParticipantRepository();

    private final ParticipantMapper participantMapper = new ParticipantMapper();
    private final CreateParticipantMapper createParticipantMapper = new CreateParticipantMapper();

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
