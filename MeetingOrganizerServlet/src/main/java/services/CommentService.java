package services;

import dao.CommentDao;
import dao.CommentLikeDao;
import dto.comment.CommentModel;
import dto.comment.CreateCommentModel;
import mappers.comment.CommentMapper;
import mappers.comment.CreateCommentMapper;
import utils.DaoManager;

import java.util.List;
import java.util.Optional;

public class CommentService {
    private final CommentDao commentDao = DaoManager.getCommentDao();
    private final CommentLikeDao commentLikeDao = DaoManager.getCommentLikeDao();

    private final CommentMapper commentMapper = new CommentMapper();
    private final CreateCommentMapper createCommentMapper = new CreateCommentMapper();

    public List<CommentModel> getAll() {
        List<CommentModel> models = commentDao.getAll()
                .stream()
                .map(commentMapper::map)
                .toList();

        for (CommentModel model : models) {
            model.setLikeCount(commentLikeDao.getLikesCount(model.getCommentId()));
        }

        return models;
    }

    public List<CommentModel> getByMeetingId(Long meetingId) {
        List<CommentModel> models = commentDao.getAllByMeetingId(meetingId)
                .stream()
                .map(commentMapper::map)
                .toList();

        for (CommentModel model : models) {
            model.setLikeCount(commentLikeDao.getLikesCount(model.getCommentId()));
        }

        return models;
    }

    public Optional<CommentModel> getById(Long commentId) {
        CommentModel model = commentDao.getById(commentId)
                .map(commentMapper::map)
                .orElse(null);

        if (model == null) {
            return Optional.empty();
        }

        model.setLikeCount(commentLikeDao.getLikesCount(model.getCommentId()));

        return Optional.of(model);
    }

    public CommentModel create(CreateCommentModel createModel) {
        return commentMapper.map(commentDao.create(createCommentMapper.map(createModel)));
    }

    public boolean update(CreateCommentModel createModel) {
        return commentDao.update(createCommentMapper.map(createModel));
    }

    public boolean delete(Long commentId) {
        return commentDao.delete(commentId);
    }
}
