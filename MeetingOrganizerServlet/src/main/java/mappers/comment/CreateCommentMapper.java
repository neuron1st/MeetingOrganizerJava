package mappers.comment;

import dto.comment.CreateCommentModel;
import entity.Comment;
import entity.Meeting;
import entity.User;
import mappers.BaseMapper;
import repositories.MeetingRepository;
import repositories.UserRepository;
import utils.BaseConnectionManager;
import utils.ConnectionManager;

public class CreateCommentMapper implements BaseMapper<CreateCommentModel, Comment> {
    private final BaseConnectionManager connectionManager = new ConnectionManager();
    private final UserRepository userRepository = new UserRepository(connectionManager);
    private final MeetingRepository meetingRepository = new MeetingRepository(connectionManager);

    @Override
    public Comment map(CreateCommentModel source) {
        User user = userRepository
                .getById(source.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Meeting meeting = meetingRepository
                .getById(source.getMeetingId())
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found"));

        return Comment.builder()
                .text(source.getText())
                .dateTime(source.getDateTime())
                .user(user)
                .meeting(meeting)
                .build();
    }
}
