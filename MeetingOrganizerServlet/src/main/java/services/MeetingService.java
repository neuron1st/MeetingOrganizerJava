package services;

import repositories.CommentRepository;
import repositories.MeetingRepository;
import repositories.MeetingLikeRepository;
import repositories.ParticipantRepository;
import dto.meeting.CreateMeetingModel;
import dto.meeting.MeetingModel;
import mappers.meeting.CreateMeetingMapper;
import mappers.meeting.MeetingMapper;
import utils.RepositoryManager;

import java.util.List;
import java.util.Optional;

public class MeetingService {
    private final MeetingRepository meetingRepository = RepositoryManager.getMeetingRepository();
    private final CommentRepository commentRepository = RepositoryManager.getCommentRepository();
    private final ParticipantRepository participantRepository = RepositoryManager.getParticipantRepository();
    private final MeetingLikeRepository meetingLikeRepository = RepositoryManager.getMeetingLikeRepository();

    private final MeetingMapper meetingMapper = new MeetingMapper();
    private final CreateMeetingMapper createMeetingMapper = new CreateMeetingMapper();

    public List<MeetingModel> getAll() {
        List<MeetingModel> models = meetingRepository.getAll()
                .stream()
                .map(meetingMapper::map)
                .toList();

        for (MeetingModel model : models) {
            model.setLikeCount(meetingLikeRepository.getLikesCount(model.getMeetingId()));
            model.setCommentCount(commentRepository.getAllByMeetingId(model.getMeetingId()).size());
            model.setParticipantCount(participantRepository.getAllByMeetingId(model.getMeetingId()).size());
        }

        return models;
    }

    public List<MeetingModel> getByTitle(String title) {
        List<MeetingModel> models = meetingRepository.getByTitle(title)
                .stream()
                .map(meetingMapper::map)
                .toList();

        for (MeetingModel model : models) {
            model.setLikeCount(meetingLikeRepository.getLikesCount(model.getMeetingId()));
            model.setCommentCount(commentRepository.getAllByMeetingId(model.getMeetingId()).size());
            model.setParticipantCount(participantRepository.getAllByMeetingId(model.getMeetingId()).size());
        }

        return models;
    }


    public Optional<MeetingModel> getById(Long meetingId) {
        MeetingModel model = meetingRepository.getById(meetingId)
                .map(meetingMapper::map)
                .orElse(null);

        if (model == null) {
            return Optional.empty();
        }

        model.setLikeCount(meetingLikeRepository.getLikesCount(model.getMeetingId()));
        model.setCommentCount(commentRepository.getAllByMeetingId(model.getMeetingId()).size());
        model.setParticipantCount(participantRepository.getAllByMeetingId(model.getMeetingId()).size());

        return Optional.of(model);
    }

    public MeetingModel create(CreateMeetingModel createModel) {
        return meetingMapper.map(meetingRepository.create(createMeetingMapper.map(createModel)));
    }

    public boolean update(CreateMeetingModel createModel) {
        return meetingRepository.update(createMeetingMapper.map(createModel));
    }

    public boolean delete(Long meetingId) {
        return meetingRepository.delete(meetingId);
    }
}
