package dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetingModel {
    private long meetingId;
    private String title;
    private String description;
    private LocalDateTime date;
    private int likeCount;
    private int participantCount;
    private int commentCount;
}
