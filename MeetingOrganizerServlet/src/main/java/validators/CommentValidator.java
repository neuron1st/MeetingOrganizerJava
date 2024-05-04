package validators;

import dto.comment.CreateCommentModel;

public class CommentValidator implements BaseValidator<CreateCommentModel> {
    @Override
    public void validate(CreateCommentModel model) {
        if (model.getText() == null)
            throw new IllegalArgumentException("Comment text must not be empty");
    }
}
