package mappers.meeting;

import dto.meeting.CreateMeetingModel;
import entity.Meeting;
import mappers.BaseMapper;

public class CreateMeetingMapper implements BaseMapper<CreateMeetingModel, Meeting> {
    @Override
    public Meeting map(CreateMeetingModel source) {
        return Meeting.builder()
                .title(source.getTitle())
                .description(source.getDescription())
                .date(source.getDate())
                .build();
    }
}
