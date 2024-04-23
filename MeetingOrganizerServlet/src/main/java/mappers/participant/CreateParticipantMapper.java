package mappers.participant;

import repositories.MeetingRepository;
import repositories.UserRepository;
import dto.participant.CreateParticipantModel;
import entity.Meeting;
import entity.Participant;
import entity.User;
import mappers.BaseMapper;
import utils.RepositoryManager;

public class CreateParticipantMapper implements BaseMapper<CreateParticipantModel, Participant> {
    private final UserRepository userRepository = RepositoryManager.getUserRepository();
    private final MeetingRepository meetingRepository = RepositoryManager.getMeetingRepository();

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
