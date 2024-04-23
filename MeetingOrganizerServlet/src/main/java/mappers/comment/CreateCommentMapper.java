package mappers.comment;

import repositories.MeetingRepository;
import repositories.UserRepository;
import dto.comment.CreateCommentModel;
import entity.Comment;
import entity.Meeting;
import entity.User;
import mappers.BaseMapper;
import utils.RepositoryManager;

public class CreateCommentMapper implements BaseMapper<CreateCommentModel, Comment> {
    private final UserRepository userRepository = RepositoryManager.getUserRepository();
    private final MeetingRepository meetingRepository = RepositoryManager.getMeetingRepository();

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
