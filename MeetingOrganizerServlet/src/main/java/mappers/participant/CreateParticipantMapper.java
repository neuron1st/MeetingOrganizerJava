package mappers.participant;

import dao.MeetingDao;
import dao.UserDao;
import dto.participant.CreateParticipantModel;
import entity.Meeting;
import entity.Participant;
import entity.User;
import mappers.BaseMapper;
import utils.DaoManager;

public class CreateParticipantMapper implements BaseMapper<CreateParticipantModel, Participant> {
    private final UserDao userDao = DaoManager.getUserDao();
    private final MeetingDao meetingDao = DaoManager.getMeetingDao();

    @Override
    public Participant map(CreateParticipantModel source) {
        User user = userDao
                .getById(source.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Meeting meeting = meetingDao
                .getById(source.getMeetingId())
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found"));

        return Participant.builder()
                .user(user)
                .meeting(meeting)
                .role(source.getRole())
                .build();
    }
}
