package mappers.participant;

import dto.participant.CreateParticipantModel;
import entity.Meeting;
import entity.Participant;
import entity.User;
import mappers.BaseMapper;
import repositories.MeetingRepository;
import repositories.UserRepository;
import utils.BaseConnectionManager;
import utils.ConnectionManager;

public class CreateParticipantMapper implements BaseMapper<CreateParticipantModel, Participant> {
    private final BaseConnectionManager connectionManager = new ConnectionManager();
    private final UserRepository userRepository = new UserRepository(connectionManager);
    private final MeetingRepository meetingRepository = new MeetingRepository(connectionManager);

    @Override
    public Participant map(CreateParticipantModel source) {
        User user = userRepository
                .getById(source.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Meeting meeting = meetingRepository
                .getById(source.getMeetingId())
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found"));

        return Participant.builder()
                .user(user)
                .meeting(meeting)
                .role(source.getRole())
                .build();
    }
}
