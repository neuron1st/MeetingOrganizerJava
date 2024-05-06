package service;

import dto.user.CreateUserModel;
import dto.user.UserModel;
import entity.User;
import mappers.user.CreateUserMapper;
import mappers.user.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repositories.UserRepository;
import services.UserService;
import utils.PasswordHash;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private CreateUserMapper createUserMapper;

    @InjectMocks
    private UserService userService;

    private static UserModel getUserModel() {
        return UserModel.builder()
                .userId(1L)
                .email("test@example.com")
                .build();
    }

    private static User getUser() {
        return User.builder()
                .userId(1L)
                .email("test@example.com")
                .password("hashedPassword")
                .build();
    }

    @Test
    public void testGetAll() {
        User user = getUser();
        List<User> users = List.of(user);
        UserModel userModel = getUserModel();

        when(userRepository.getAll()).thenReturn(users);
        when(userMapper.map(any())).thenReturn(userModel);

        Assertions.assertEquals(userService.getAll().size(), 1);
        Assertions.assertEquals(userService.getAll().get(0).getUserId(), userModel.getUserId());
    }

    @Test
    public void testGetById() {
        long userId = 1L;
        User user = getUser();
        UserModel userModel = getUserModel();

        when(userRepository.getById(userId)).thenReturn(Optional.of(user));
        when(userMapper.map(user)).thenReturn(userModel);

        Assertions.assertTrue(userService.getById(userId).isPresent());
        Assertions.assertEquals(userService.getById(userId).get().getUserId(), userModel.getUserId());
    }

    @Test
    public void testGetByEmail() {
        String email = "test@example.com";
        User user = getUser();
        UserModel userModel = getUserModel();

        when(userRepository.getByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.map(user)).thenReturn(userModel);

        Assertions.assertTrue(userService.getByEmail(email).isPresent());
        Assertions.assertEquals(userService.getByEmail(email).get().getUserId(), userModel.getUserId());
    }

    @Test
    public void testCheckLoginWithCorrectCredentials() {
        String email = "test@example.com";
        String password = "password";
        String hashedPassword = PasswordHash.hashPassword(password);

        User user = getUser();
        user.setPassword(hashedPassword);

        when(userRepository.getByEmail(email)).thenReturn(Optional.of(user));

        Assertions.assertTrue(userService.checkLogin(email, password).isEmpty());
    }

    @Test
    public void testCheckLoginWithIncorrectEmail() {
        String email = "nonexistent@example.com";
        String password = "password";

        when(userRepository.getByEmail(email)).thenReturn(Optional.empty());

        Assertions.assertTrue(userService.checkLogin(email, password).isPresent());
    }

    @Test
    public void testCheckLoginWithIncorrectPassword() {
        String email = "test@example.com";
        String password = "incorrectPassword";
        String hashedPassword = PasswordHash.hashPassword("password");

        User user = getUser();
        user.setPassword(hashedPassword);

        when(userRepository.getByEmail(email)).thenReturn(Optional.of(user));

        Assertions.assertTrue(userService.checkLogin(email, password).isPresent());
    }

    @Test
    public void testCreate() {
        CreateUserModel createModel = CreateUserModel.builder()
                .email("test@example.com")
                .password("password")
                .build();
        User user = getUser();
        UserModel userModel = getUserModel();

        when(createUserMapper.map(createModel)).thenReturn(user);
        when(userRepository.create(user)).thenReturn(user);
        when(userMapper.map(user)).thenReturn(userModel);

        Assertions.assertEquals(userService.create(createModel).getUserId(), userModel.getUserId());
    }

    @Test
    public void testUpdate() {
        CreateUserModel createModel = CreateUserModel.builder()
                .email("test@example.com")
                .password("password")
                .build();
        User user = getUser();

        when(createUserMapper.map(createModel)).thenReturn(user);
        when(userRepository.update(user)).thenReturn(true);

        Assertions.assertTrue(userService.update(createModel));
    }

    @Test
    public void testDelete() {
        Long userId = 1L;
        when(userRepository.delete(userId)).thenReturn(true);

        Assertions.assertTrue(userService.delete(userId));
    }
}
