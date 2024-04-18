package mappers.comment;

import dto.comment.CommentModel;
import entity.Comment;
import mappers.BaseMapper;

public class CommentMapper implements BaseMapper<Comment, CommentModel> {
    @Override
    public CommentModel map(Comment source) {
        return CommentModel.builder()
                .commentId(source.getCommentId())
                .text(source.getText())
                .dateTime(source.getDateTime())
                .userName(source.getUser().getFullName())
                .meetingName(source.getMeeting().getTitle())
                .likeCount(null)
                .build();
    }
}
