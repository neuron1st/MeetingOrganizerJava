package services;

import dao.ParticipantDao;
import dto.meeting.MeetingModel;
import dto.participant.CreateParticipantModel;
import dto.participant.ParticipantModel;
import mappers.participant.CreateParticipantMapper;
import mappers.participant.ParticipantMapper;
import utils.DaoManager;

import java.util.List;

public class ParticipantService {
    private final ParticipantDao participantDao = DaoManager.getParticipantDao();

    private final ParticipantMapper participantMapper = new ParticipantMapper();
    private final CreateParticipantMapper createParticipantMapper = new CreateParticipantMapper();

    public List<ParticipantModel> getAll() {
        return participantDao.getAll()
                .stream()
                .map(participantMapper::map)
                .toList();
    }

    public List<ParticipantModel> getAllByMeetingId(Long meetingId) {
        return participantDao.getAllByMeetingId(meetingId)
                .stream()
                .map(participantMapper::map)
                .toList();
    }

    public List<ParticipantModel> getAllByUserId(Long userId) {
        return participantDao.getAllByMeetingId(userId)
                .stream()
                .map(participantMapper::map)
                .toList();
    }

    public ParticipantModel create(CreateParticipantModel createModel) {
        return participantMapper.map(participantDao.create(createParticipantMapper.map(createModel)));
    }

    public boolean delete(Long meetingId, Long userId) {
        return participantDao.delete(meetingId, userId);
    }
}
