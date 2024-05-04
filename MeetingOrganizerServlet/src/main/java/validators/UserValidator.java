package validators;

import dto.user.CreateUserModel;

import java.util.regex.Pattern;

public class UserValidator implements BaseValidator<CreateUserModel> {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Override
    public void validate(CreateUserModel model) {
        if (!EMAIL_PATTERN.matcher(model.getEmail()).matches())
            throw new IllegalArgumentException("Wrong email format");
        if (model.getEmail() == null)
            throw new IllegalArgumentException("Email is required");
        if (model.getFullName() == null)
            throw new IllegalArgumentException("Name is required");
        if (model.getFullName().length() < 3)
            throw new IllegalArgumentException("Name is too short");
        if (model.getPassword().length() < 6)
            throw new IllegalArgumentException("Password is too short");
    }
}
