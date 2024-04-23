package utils;

import repositories.CommentRepository;
import repositories.CommentLikeRepository;
import repositories.MeetingRepository;
import repositories.MeetingLikeRepository;
import repositories.ParticipantRepository;
import repositories.UserRepository;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RepositoryManager {
    @Getter
    private static final UserRepository userRepository;
    @Getter
    private static final MeetingRepository meetingRepository;
    @Getter
    private static final ParticipantRepository participantRepository;
    @Getter
    private static final CommentRepository commentRepository;
    @Getter
    private static final CommentLikeRepository commentLikeRepository;
    @Getter
    private static final MeetingLikeRepository meetingLikeRepository;

    static {
        userRepository = new UserRepository();
        meetingRepository = new MeetingRepository();
        participantRepository = new ParticipantRepository(userRepository, meetingRepository);
        commentRepository = new CommentRepository(userRepository, meetingRepository);
        commentLikeRepository = new CommentLikeRepository();
        meetingLikeRepository = new MeetingLikeRepository();
    }
}
