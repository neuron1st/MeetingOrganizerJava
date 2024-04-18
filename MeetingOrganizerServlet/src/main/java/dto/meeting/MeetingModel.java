package dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MeetingModel {
    private Long meetingId;
    private String title;
    private String description;
    private LocalDateTime date;
    private Integer likeCount;
    private Integer participantCount;
    private Integer commentCount;
}
