package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Comment {
    private long commentId;
    private String text;
    private LocalDateTime dateTime;
    private User user;
    private Meeting meeting;
}
