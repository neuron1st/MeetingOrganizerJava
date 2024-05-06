package services;

import dto.comment.CommentModel;
import dto.comment.CreateCommentModel;
import mappers.comment.CommentMapper;
import mappers.comment.CreateCommentMapper;
import repositories.CommentLikeRepository;
import repositories.CommentRepository;
import repositories.MeetingRepository;
import repositories.UserRepository;
import utils.BaseConnectionManager;
import utils.ConnectionManager;

import java.util.List;
import java.util.Optional;

public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentMapper commentMapper;
    private final CreateCommentMapper createCommentMapper;

    public CommentService(
            CommentRepository commentRepository,
            CommentLikeRepository commentLikeRepository,
            CommentMapper commentMapper,
            CreateCommentMapper createCommentMapper
    ) {
        this.commentRepository = commentRepository;
        this.commentLikeRepository = commentLikeRepository;
        this.commentMapper = commentMapper;
        this.createCommentMapper = createCommentMapper;
    }


    public List<CommentModel> getAll() {
        List<CommentModel> models = commentRepository.getAll()
                .stream()
                .map(commentMapper::map)
                .toList();

        for (CommentModel model : models) {
            model.setLikeCount(commentLikeRepository.getLikesCount(model.getCommentId()));
        }

        return models;
    }

    public List<CommentModel> getByMeetingId(Long meetingId) {
        List<CommentModel> models = commentRepository.getAllByMeetingId(meetingId)
                .stream()
                .map(commentMapper::map)
                .toList();

        for (CommentModel model : models) {
            model.setLikeCount(commentLikeRepository.getLikesCount(model.getCommentId()));
        }

        return models;
    }

    public Optional<CommentModel> getById(Long commentId) {
        CommentModel model = commentRepository.getById(commentId)
                .map(commentMapper::map)
                .orElse(null);

        if (model == null) {
            return Optional.empty();
        }

        model.setLikeCount(commentLikeRepository.getLikesCount(model.getCommentId()));

        return Optional.of(model);
    }

    public CommentModel create(CreateCommentModel createModel) {
        return commentMapper.map(commentRepository.create(createCommentMapper.map(createModel)));
    }

    public boolean update(CreateCommentModel createModel) {
        return commentRepository.update(createCommentMapper.map(createModel));
    }

    public boolean delete(Long commentId) {
        return commentRepository.delete(commentId);
    }
}
