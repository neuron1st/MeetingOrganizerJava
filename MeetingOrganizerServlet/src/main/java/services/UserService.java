package services;

import dto.user.CreateUserModel;
import dto.user.UserModel;
import entity.User;
import mappers.user.CreateUserMapper;
import mappers.user.UserMapper;
import repositories.UserRepository;
import utils.PasswordHash;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CreateUserMapper createUserMapper;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            CreateUserMapper createUserMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.createUserMapper = createUserMapper;
    }

    public List<UserModel> getAll() {
        return userRepository.getAll()
                .stream()
                .map(userMapper::map)
                .toList();
    }

    public Optional<UserModel> getById(long userId) {
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

    public boolean update(long userId, CreateUserModel createModel) {
        Optional<User> userOpt = userRepository.getById(userId);

        if (userOpt.isEmpty())
            return false;

        User user = userOpt.get();
        user.setEmail(createModel.getEmail());
        user.setFullName(createModel.getFullName());

        return userRepository.update(user);
    }

    public boolean delete(long userId) {
        return userRepository.delete(userId);
    }
}
