package services;

import dao.CommentDao;
import dao.MeetingDao;
import dao.MeetingLikeDao;
import dao.ParticipantDao;
import dto.meeting.CreateMeetingModel;
import dto.meeting.MeetingModel;
import mappers.meeting.CreateMeetingMapper;
import mappers.meeting.MeetingMapper;
import utils.DaoManager;

import java.util.List;
import java.util.Optional;

public class MeetingService {
    private final MeetingDao meetingDao = DaoManager.getMeetingDao();
    private final CommentDao commentDao = DaoManager.getCommentDao();
    private final ParticipantDao participantDao = DaoManager.getParticipantDao();
    private final MeetingLikeDao meetingLikeDao = DaoManager.getMeetingLikeDao();

    private final MeetingMapper meetingMapper = new MeetingMapper();
    private final CreateMeetingMapper createMeetingMapper = new CreateMeetingMapper();

    public List<MeetingModel> getAll() {
        List<MeetingModel> models = meetingDao.getAll()
                .stream()
                .map(meetingMapper::map)
                .toList();

        for (MeetingModel model : models) {
            model.setLikeCount(meetingLikeDao.getLikesCount(model.getMeetingId()));
            model.setCommentCount(commentDao.getAllByMeetingId(model.getMeetingId()).size());
            model.setParticipantCount(participantDao.getAllByMeetingId(model.getMeetingId()).size());
        }

        return models;
    }

    public List<MeetingModel> getByTitle(String title) {
        List<MeetingModel> models = meetingDao.getByTitle(title)
                .stream()
                .map(meetingMapper::map)
                .toList();

        for (MeetingModel model : models) {
            model.setLikeCount(meetingLikeDao.getLikesCount(model.getMeetingId()));
            model.setCommentCount(commentDao.getAllByMeetingId(model.getMeetingId()).size());
            model.setParticipantCount(participantDao.getAllByMeetingId(model.getMeetingId()).size());
        }

        return models;
    }


    public Optional<MeetingModel> getById(Long meetingId) {
        MeetingModel model = meetingDao.getById(meetingId)
                .map(meetingMapper::map)
                .orElse(null);

        if (model == null) {
            return Optional.empty();
        }

        model.setLikeCount(meetingLikeDao.getLikesCount(model.getMeetingId()));
        model.setCommentCount(commentDao.getAllByMeetingId(model.getMeetingId()).size());
        model.setParticipantCount(participantDao.getAllByMeetingId(model.getMeetingId()).size());

        return Optional.of(model);
    }

    public MeetingModel create(CreateMeetingModel createModel) {
        return meetingMapper.map(meetingDao.create(createMeetingMapper.map(createModel)));
    }

    public boolean update(CreateMeetingModel createModel) {
        return meetingDao.update(createMeetingMapper.map(createModel));
    }

    public boolean delete(Long meetingId) {
        return meetingDao.delete(meetingId);
    }
}
