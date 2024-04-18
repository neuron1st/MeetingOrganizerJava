package utils;

import dao.CommentDao;
import dao.CommentLikeDao;
import dao.MeetingDao;
import dao.MeetingLikeDao;
import dao.ParticipantDao;
import dao.UserDao;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DaoManager {
    @Getter
    private static final UserDao userDao;
    @Getter
    private static final MeetingDao meetingDao;
    @Getter
    private static final ParticipantDao participantDao;
    @Getter
    private static final CommentDao commentDao;
    @Getter
    private static final CommentLikeDao commentLikeDao;
    @Getter
    private static final MeetingLikeDao meetingLikeDao;

    static {
        userDao = new UserDao();
        meetingDao = new MeetingDao();
        participantDao = new ParticipantDao(userDao, meetingDao);
        commentDao = new CommentDao(userDao, meetingDao);
        commentLikeDao = new CommentLikeDao();
        meetingLikeDao = new MeetingLikeDao();
    }
}
