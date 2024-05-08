package service;

import dto.comment.CommentModel;
import dto.comment.CreateCommentModel;
import entity.Comment;
import entity.Meeting;
import entity.User;
import mappers.comment.CommentMapper;
import mappers.comment.CreateCommentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import repositories.CommentLikeRepository;
import repositories.CommentRepository;
import services.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentLikeRepository commentLikeRepository;
    @Spy
    private CommentMapper commentMapper = new CommentMapper();
    @Spy
    private CreateCommentMapper createCommentMapper = new CreateCommentMapper();

    @InjectMocks
    private CommentService commentService;


    private static Meeting getMeeting() {
        return Meeting.builder()
                .meetingId(1)
                .title("Test")
                .description("Test desc")
                .date(LocalDateTime.now())
                .build();
    }

    private static User getUser() {
        return User.builder()
                .userId(1L)
                .fullName("Test")
                .email("test@example.com")
                .password("hashedPassword")
                .build();
    }

    private static Comment getComment() {
        return Comment.builder()
                .commentId(1L)
                .text("Test")
                .user(getUser())
                .meeting(getMeeting())
                .dateTime(LocalDateTime.now())
                .build();
    }

    private static CommentModel getCommentModel(Comment comment) {
        return CommentModel.builder()
                .commentId(comment.getCommentId())
                .text(comment.getText())
                .userName(comment.getUser().getFullName())
                .meetingName(comment.getMeeting().getTitle())
                .likeCount(0)
                .dateTime(comment.getDateTime())
                .build();
    }

    @Test
    public void testGetAll() {
        Comment comment = getComment();

        CommentModel commentModel = getCommentModel(comment);

        when(commentRepository.getAll()).thenReturn(List.of(comment));
        when(commentLikeRepository.getLikesCount(anyLong())).thenReturn(0);

        List<CommentModel> comments = commentService.getAll();

        assertEquals(1, comments.size());
        assertCommentsEqual(commentModel, comments.get(0));
    }

    @Test
    public void testGetByMeetingId() {
        Comment comment = getComment();
        CommentModel commentModel = getCommentModel(comment);
        long meetingId = comment.getMeeting().getMeetingId();

        when(commentRepository.getAllByMeetingId(meetingId)).thenReturn(List.of(comment));
        when(commentLikeRepository.getLikesCount(anyLong())).thenReturn(0);

        List<CommentModel> comments = commentService.getByMeetingId(meetingId);

        assertEquals(1, comments.size());
        assertCommentsEqual(commentModel, comments.get(0));
    }

    @Test
    public void testGetById() {
        long commentId = 1L;
        when(commentRepository.getById(commentId)).thenReturn(Optional.empty());

        assertFalse(commentService.getById(commentId).isPresent());
    }

    @Test
    public void testCreate() {
        Comment comment = getComment();
        CommentModel commentModel = getCommentModel(comment);
        CreateCommentModel createModel = CreateCommentModel.builder()
                .text(getComment().getText())
                .userId(getUser().getUserId())
                .meetingId(getMeeting().getMeetingId())
                .build();

        when(commentRepository.create(any())).thenReturn(comment);

        assertCommentsEqual(commentService.create(createModel), commentModel);
    }

    @Test
    public void testUpdate() {
        Comment comment = getComment();
        CreateCommentModel createModel = CreateCommentModel.builder()
                .text("Updated text")
                .userId(comment.getUser().getUserId())
                .meetingId(comment.getMeeting().getMeetingId())
                .build();

        when(commentRepository.update(comment)).thenReturn(true);
        when(commentRepository.getById(comment.getCommentId())).thenReturn(Optional.of(comment));

        assertTrue(commentService.update(comment.getCommentId(), createModel));
    }

    @Test
    public void testDelete() {
        long commentId = 1L;
        when(commentRepository.delete(commentId)).thenReturn(true);

        assertTrue(commentService.delete(commentId));
    }

    private static void assertCommentsEqual(CommentModel expected, CommentModel actual) {
        assertEquals(expected.getCommentId(), actual.getCommentId());
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getDateTime(), actual.getDateTime());
        assertEquals(expected.getLikeCount(), actual.getLikeCount());
        assertEquals(expected.getMeetingName(), actual.getMeetingName());
        assertEquals(expected.getUserName(), actual.getUserName());
    }
}
