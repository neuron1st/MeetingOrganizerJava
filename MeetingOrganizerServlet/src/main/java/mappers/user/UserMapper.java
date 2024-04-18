package mappers.user;

import dto.user.UserModel;
import entity.User;
import mappers.BaseMapper;

public class UserMapper implements BaseMapper<User, UserModel> {
    @Override
    public UserModel map(User source) {
        return UserModel.builder()
                .userId(source.getUserId())
                .fullName(source.getFullName())
                .email(source.getEmail())
                .build();
    }
}
