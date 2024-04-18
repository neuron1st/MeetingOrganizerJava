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
public class CommentModel {
    private Long commentId;
    private String text;
    private LocalDateTime dateTime;
    private String userName;
    private String meetingName;
    private Integer likeCount;
}
