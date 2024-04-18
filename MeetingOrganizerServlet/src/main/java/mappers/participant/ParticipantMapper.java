package mappers.participant;

import dto.participant.ParticipantModel;
import entity.Participant;
import mappers.BaseMapper;

public class ParticipantMapper implements BaseMapper<Participant, ParticipantModel> {
    @Override
    public ParticipantModel map(Participant source) {
        return ParticipantModel.builder()
                .userName(source.getUser().getFullName())
                .meetingName(source.getMeeting().getTitle())
                .role(source.getRole())
                .build();
    }
}