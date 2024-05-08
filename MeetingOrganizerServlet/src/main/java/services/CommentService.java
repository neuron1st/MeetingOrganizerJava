package services;

import dto.comment.CommentModel;
import dto.comment.CreateCommentModel;
import entity.Comment;
import mappers.comment.CommentMapper;
import mappers.comment.CreateCommentMapper;
import repositories.CommentLikeRepository;
import repositories.CommentRepository;

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

    public List<CommentModel> getByMeetingId(long meetingId) {
        List<CommentModel> models = commentRepository.getAllByMeetingId(meetingId)
                .stream()
                .map(commentMapper::map)
                .toList();

        for (CommentModel model : models) {
            model.setLikeCount(commentLikeRepository.getLikesCount(model.getCommentId()));
        }

        return models;
    }

    public Optional<CommentModel> getById(long commentId) {
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

    public boolean update(long commentId, CreateCommentModel createModel) {
        Optional<Comment> commentOpt = commentRepository.getById(commentId);

        if (commentOpt.isEmpty()) {
            return false;
        }

        Comment comment = commentOpt.get();
        comment.setText(createModel.getText());

        return commentRepository.update(comment);
    }

    public boolean delete(long commentId) {
        return commentRepository.delete(commentId);
    }
}
