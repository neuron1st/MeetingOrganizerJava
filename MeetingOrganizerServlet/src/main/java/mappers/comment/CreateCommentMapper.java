package mappers.comment;

import dao.MeetingDao;
import dao.UserDao;
import dto.comment.CreateCommentModel;
import entity.Comment;
import entity.Meeting;
import entity.User;
import mappers.BaseMapper;
import utils.DaoManager;

public class CreateCommentMapper implements BaseMapper<CreateCommentModel, Comment> {
    private final UserDao userDao = DaoManager.getUserDao();
    private final MeetingDao meetingDao = DaoManager.getMeetingDao();

    @Override
    public Comment map(CreateCommentModel source) {
        User user = userDao
                .getById(source.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Meeting meeting = meetingDao
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
