package services;

import dao.UserDao;
import dto.user.CreateUserModel;
import dto.user.UserModel;
import entity.User;
import mappers.user.CreateUserMapper;
import mappers.user.UserMapper;
import utils.DaoManager;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDao userDao = DaoManager.getUserDao();
    private final UserMapper userMapper = new UserMapper();
    private final CreateUserMapper createUserMapper = new CreateUserMapper();

    public List<UserModel> getAll() {
        return userDao.getAll()
                .stream()
                .map(userMapper::map)
                .toList();
    }

    public Optional<UserModel> getById(Long userId) {
        UserModel model = userDao.getById(userId)
                .map(userMapper::map)
                .orElse(null);

        if (model == null) {
            return Optional.empty();
        }

        return Optional.of(model);
    }

    public UserModel create(CreateUserModel createModel) {
        return userMapper.map(userDao.create(createUserMapper.map(createModel)));
    }

    public boolean update(CreateUserModel createModel) {
        return userDao.update(createUserMapper.map(createModel));
    }

    private boolean delete(Long userId) {
        return userDao.delete(userId);
    }
}
