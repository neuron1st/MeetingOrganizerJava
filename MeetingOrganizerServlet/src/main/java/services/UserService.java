package services;

import entity.User;
import repositories.UserRepository;
import dto.user.CreateUserModel;
import dto.user.UserModel;
import mappers.user.CreateUserMapper;
import mappers.user.UserMapper;
import utils.PasswordHash;
import utils.RepositoryManager;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository = RepositoryManager.getUserRepository();
    private final UserMapper userMapper = new UserMapper();
    private final CreateUserMapper createUserMapper = new CreateUserMapper();

    public List<UserModel> getAll() {
        return userRepository.getAll()
                .stream()
                .map(userMapper::map)
                .toList();
    }

    public Optional<UserModel> getById(Long userId) {
        UserModel model = userRepository.getById(userId)
                .map(userMapper::map)
                .orElse(null);

        if (model == null) {
            return Optional.empty();
        }

        return Optional.of(model);
    }

    public Optional<UserModel> getByEmail(String email) {
        UserModel model = userRepository.getByEmail(email)
                .map(userMapper::map)
                .orElse(null);

        if (model == null) {
            return Optional.empty();
        }

        return Optional.of(model);
    }

    public Optional<String> checkLogin(String email, String password) {
        User user = userRepository.getByEmail(email)
                .orElse(null);

        if (user == null || !PasswordHash.checkPassword(password, user.getPassword())) {
            return Optional.of("Incorrect email or password");
        }

        return Optional.empty();
    }

    public UserModel create(CreateUserModel createModel) throws IllegalArgumentException {
        String hashedPassword = PasswordHash.hashPassword(createModel.getPassword());
        createModel.setPassword(hashedPassword);
        return userMapper.map(userRepository.create(createUserMapper.map(createModel)));
    }

    public boolean update(CreateUserModel createModel) {
        return userRepository.update(createUserMapper.map(createModel));
    }

    public boolean delete(Long userId) {
        return userRepository.delete(userId);
    }
}
