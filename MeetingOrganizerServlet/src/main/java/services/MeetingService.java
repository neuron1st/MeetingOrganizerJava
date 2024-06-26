package services;

import dto.meeting.CreateMeetingModel;
import dto.meeting.MeetingModel;
import entity.Meeting;
import mappers.meeting.CreateMeetingMapper;
import mappers.meeting.MeetingMapper;
import repositories.CommentRepository;
import repositories.MeetingLikeRepository;
import repositories.MeetingRepository;
import repositories.ParticipantRepository;

import java.util.List;
import java.util.Optional;

public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final CommentRepository commentRepository;
    private final ParticipantRepository participantRepository;
    private final MeetingLikeRepository meetingLikeRepository;

    private final MeetingMapper meetingMapper;
    private final CreateMeetingMapper createMeetingMapper;

    public MeetingService(
            MeetingRepository meetingRepository,
            CommentRepository commentRepository,
            ParticipantRepository participantRepository,
            MeetingLikeRepository meetingLikeRepository,
            MeetingMapper meetingMapper,
            CreateMeetingMapper createMeetingMapper
    ) {
        this.meetingRepository = meetingRepository;
        this.commentRepository = commentRepository;
        this.participantRepository = participantRepository;
        this.meetingLikeRepository = meetingLikeRepository;
        this.meetingMapper = meetingMapper;
        this.createMeetingMapper = createMeetingMapper;
    }

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


    public Optional<MeetingModel> getById(long meetingId) {
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

    public boolean update(long meetingId, CreateMeetingModel createModel) {
        Optional<Meeting> meetingOpt = meetingRepository.getById(meetingId);

        if (meetingOpt.isEmpty()) {
            return false;
        }

        Meeting meeting = meetingOpt.get();
        meeting.setTitle(createModel.getTitle());
        meeting.setDescription(createModel.getDescription());
        meeting.setDate(createModel.getDate());

        return meetingRepository.update(meeting);
    }

    public boolean delete(long meetingId) {
        return meetingRepository.delete(meetingId);
    }
}
