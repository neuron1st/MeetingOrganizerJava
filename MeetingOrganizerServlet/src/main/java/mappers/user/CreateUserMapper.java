package mappers.user;

import dto.user.CreateUserModel;
import entity.User;
import mappers.BaseMapper;

public class CreateUserMapper implements BaseMapper<CreateUserModel, User> {
    @Override
    public User map(CreateUserModel source) {
        return User.builder()
                .fullName(source.getFullName())
                .email(source.getEmail())
                .password(source.getPassword())
                .build();
    }
}
