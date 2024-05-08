package repository;

import entity.Comment;
import entity.Meeting;
import entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repositories.CommentRepository;
import repositories.MeetingRepository;
import repositories.UserRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CommentRepositoryTest {
    private final TestConnectionManager connectionManager = new TestConnectionManager();
    private final UserRepository userRepository = new UserRepository(connectionManager);
    private final MeetingRepository meetingRepository = new MeetingRepository(connectionManager);
    private final CommentRepository commentRepository = new CommentRepository(connectionManager, userRepository, meetingRepository);

    private User createUser() {
        return User.builder()
                .fullName("Test User")
                .email("test@example.com")
                .build();
    }

    private Meeting createMeeting() {
        return Meeting.builder()
                .title("Sample Meeting")
                .description("This is a sample meeting")
                .date(LocalDateTime.now())
                .build();
    }

    private Comment createSampleComment(User user, Meeting meeting) {
        return Comment.builder()
                .text("Sample Comment")
                .dateTime(LocalDateTime.now())
                .user(user)
                .meeting(meeting)
                .build();
    }

    @BeforeEach
    @AfterEach
    public void clear() {
        try (Connection connection = connectionManager.getConnection()) {
            String CLEAR_TABLE = "TRUNCATE comments, users, meetings RESTART IDENTITY CASCADE";
            connection.prepareStatement(CLEAR_TABLE).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Create Comment - Success")
    public void testCreateComment_Success() {
        User user = userRepository.create(createUser());
        Meeting meeting = meetingRepository.create(createMeeting());

        Comment comment = createSampleComment(user, meeting);
        Comment createdComment = commentRepository.create(comment);

        assertEquals(comment.getText(), createdComment.getText());
        assertEquals(comment.getDateTime(), createdComment.getDateTime());
        assertEquals(comment.getUser().getUserId(), createdComment.getUser().getUserId());
        assertEquals(comment.getMeeting().getMeetingId(), createdComment.getMeeting().getMeetingId());
    }

    @Test
    @DisplayName("Get Comment By Id - Success")
    public void testGetCommentById_Success() {
        User user = userRepository.create(createUser());
        Meeting meeting = meetingRepository.create(createMeeting());

        Comment comment = createSampleComment(user, meeting);
        Comment createdComment = commentRepository.create(comment);

        Optional<Comment> retrievedCommentOpt = commentRepository.getById(createdComment.getCommentId());
        assertTrue(retrievedCommentOpt.isPresent());

        Comment retrievedComment = retrievedCommentOpt.get();
        assertCommentsEqual(createdComment, retrievedComment);
    }

    @Test
    @DisplayName("Get Comment By Id - Not Found")
    public void testGetCommentById_NotFound() {
        Optional<Comment> retrievedCommentOpt = commentRepository.getById(-1L);
        assertFalse(retrievedCommentOpt.isPresent());
    }

    @Test
    @DisplayName("Update Comment - Success")
    public void testUpdateComment_Success() {
        User user = userRepository.create(createUser());
        Meeting meeting = meetingRepository.create(createMeeting());

        Comment comment = createSampleComment(user, meeting);
        Comment createdComment = commentRepository.create(comment);

        createdComment.setText("Updated Comment");
        createdComment.setDateTime(LocalDateTime.now().plusDays(1));

        assertTrue(commentRepository.update(createdComment));
        Optional<Comment> updatedCommentOpt = commentRepository.getById(createdComment.getCommentId());
        assertTrue(updatedCommentOpt.isPresent());

        Comment updatedComment = updatedCommentOpt.get();
        assertCommentsEqual(createdComment, updatedComment);
    }

    @Test
    @DisplayName("Delete Comment - Success")
    public void testDeleteComment_Success() {
        User user = userRepository.create(createUser());
        Meeting meeting = meetingRepository.create(createMeeting());

        Comment comment = createSampleComment(user, meeting);
        Comment createdComment = commentRepository.create(comment);

        assertTrue(commentRepository.delete(createdComment.getCommentId()));
        Optional<Comment> deletedCommentOpt = commentRepository.getById(createdComment.getCommentId());
        assertFalse(deletedCommentOpt.isPresent());
    }

    @Test
    @DisplayName("Get All Comments - Success")
    public void testGetAllComments_Success() {
        User user = userRepository.create(createUser());
        Meeting meeting = meetingRepository.create(createMeeting());

        Comment comment1 = createSampleComment(user, meeting);
        Comment comment2 = createSampleComment(user, meeting);

        commentRepository.create(comment1);
        commentRepository.create(comment2);

        List<Comment> comments = commentRepository.getAll();
        assertEquals(2, comments.size());
    }

    @Test
    @DisplayName("Get All Comments By Meeting Id - Success")
    public void testGetAllCommentsByMeetingId_Success() {
        User user = userRepository.create(createUser());
        Meeting meeting = meetingRepository.create(createMeeting());

        Comment comment1 = createSampleComment(user, meeting);
        Comment comment2 = createSampleComment(user, meeting);

        commentRepository.create(comment1);
        commentRepository.create(comment2);

        List<Comment> comments = commentRepository.getAllByMeetingId(comment1.getMeeting().getMeetingId());
        assertEquals(2, comments.size());
    }

    @Test
    @DisplayName("Update Comment - Not Found")
    public void testUpdateComment_NotFound() {
        User user = userRepository.create(createUser());
        Meeting meeting = meetingRepository.create(createMeeting());

        Comment comment = createSampleComment(user, meeting);
        comment.setCommentId(-1L);

        assertFalse(commentRepository.update(comment));
    }

    @Test
    @DisplayName("Delete Comment - Not Found")
    public void testDeleteComment_NotFound() {
        assertFalse(commentRepository.delete(-1L));
    }

    private static void assertCommentsEqual(Comment expected, Comment actual) {
        assertEquals(expected.getCommentId(), actual.getCommentId());
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getDateTime().truncatedTo(ChronoUnit.SECONDS), actual.getDateTime().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(expected.getUser().getUserId(), actual.getUser().getUserId());
        assertEquals(expected.getMeeting().getMeetingId(), actual.getMeeting().getMeetingId());
    }
}
