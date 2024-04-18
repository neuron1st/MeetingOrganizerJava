package dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreateCommentModel {
    private String text;
    private LocalDateTime dateTime;
    private Long userId;
    private Long meetingId;
}
