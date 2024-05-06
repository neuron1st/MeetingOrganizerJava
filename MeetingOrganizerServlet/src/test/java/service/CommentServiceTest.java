package service;

import dto.comment.CommentModel;
import dto.comment.CreateCommentModel;
import entity.Comment;
import entity.Meeting;
import entity.User;
import mappers.comment.CommentMapper;
import mappers.comment.CreateCommentMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repositories.CommentLikeRepository;
import repositories.CommentRepository;
import services.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentLikeRepository commentLikeRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private CreateCommentMapper createCommentMapper;

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
                .email("test@example.com")
                .password("hashedPassword")
                .build();
    }

    @Test
    public void testGetAll() {
        CommentModel commentModel = CommentModel.builder()
                .commentId(1L)
                .text("Test")
                .userName(getUser().getFullName())
                .meetingName(getMeeting().getTitle())
                .likeCount(0)
                .build();

        Comment comment = Comment.builder()
                .commentId(1L)
                .text("Test")
                .user(getUser())
                .meeting(getMeeting())
                .dateTime(LocalDateTime.now())
                .build();

        when(commentRepository.getAll()).thenReturn(List.of(comment));
        when(commentLikeRepository.getLikesCount(anyLong())).thenReturn(0);
        when(commentMapper.map(comment)).thenReturn(commentModel);

        List<CommentModel> comments = commentService.getAll();

        Assertions.assertEquals(1, comments.size());
        Assertions.assertEquals(commentModel, comments.get(0));
    }

    @Test
    public void testGetByMeetingId() {
        long meetingId = 1L;
        CommentModel commentModel = CommentModel.builder()
                .commentId(1L)
                .text("Test")
                .userName(getUser().getFullName())
                .meetingName(getMeeting().getTitle())
                .likeCount(0)
                .build();

        Comment comment = Comment.builder()
                .commentId(1L)
                .text("Test")
                .user(getUser())
                .meeting(getMeeting())
                .dateTime(LocalDateTime.now())
                .build();

        when(commentRepository.getAllByMeetingId(meetingId)).thenReturn(List.of(comment));
        when(commentLikeRepository.getLikesCount(any())).thenReturn(0);
        when(commentMapper.map(comment)).thenReturn(commentModel);

        List<CommentModel> comments = commentService.getByMeetingId(meetingId);

        Assertions.assertEquals(1, comments.size());
        Assertions.assertEquals(commentModel, comments.get(0));
    }

    @Test
    public void testGetById() {
        long commentId = 1L;
        when(commentRepository.getById(commentId)).thenReturn(Optional.empty());

        Assertions.assertFalse(commentService.getById(commentId).isPresent());
    }

    @Test
    public void testCreate() {
        CreateCommentModel createModel = CreateCommentModel.builder()
                .text("Test")
                .userId(1L)
                .meetingId(1L)
                .build();

        CommentModel commentModel = CommentModel.builder()
                .commentId(1L)
                .text("Test")
                .userName(getUser().getFullName())
                .meetingName(getMeeting().getTitle())
                .likeCount(0)
                .build();

        Comment comment = Comment.builder()
                .commentId(1L)
                .text("Test")
                .user(getUser())
                .meeting(getMeeting())
                .dateTime(LocalDateTime.now())
                .build();


        when(createCommentMapper.map(createModel)).thenReturn(comment);
        when(commentRepository.create(any())).thenReturn(comment);
        when(commentMapper.map(any())).thenReturn(commentModel);

        Assertions.assertEquals(commentService.create(createModel), commentModel);
    }

    @Test
    public void testUpdate() {
        CreateCommentModel createModel = CreateCommentModel.builder()
                .text("Updated text")
                .userId(1L)
                .meetingId(1L)
                .build();

        when(commentRepository.update(any())).thenReturn(true);

        Assertions.assertTrue(commentService.update(createModel));
    }

    @Test
    public void testDelete() {
        long commentId = 1L;
        when(commentRepository.delete(commentId)).thenReturn(true);

        Assertions.assertTrue(commentService.delete(commentId));
    }
}
