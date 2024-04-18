package mappers.meeting;

import dto.meeting.MeetingModel;
import entity.Meeting;
import mappers.BaseMapper;

public class MeetingMapper implements BaseMapper<Meeting, MeetingModel> {
    @Override
    public MeetingModel map(Meeting source) {
        return MeetingModel.builder()
                .meetingId(source.getMeetingId())
                .title(source.getTitle())
                .description(source.getDescription())
                .date(source.getDate())
                .likeCount(null)
                .participantCount(null)
                .commentCount(null)
                .build();
    }
}