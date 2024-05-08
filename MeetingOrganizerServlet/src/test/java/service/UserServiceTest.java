package service;

import dto.user.CreateUserModel;
import dto.user.UserModel;
import entity.User;
import mappers.user.CreateUserMapper;
import mappers.user.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import repositories.UserRepository;
import services.UserService;
import utils.PasswordHash;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Spy
    private UserMapper userMapper = new UserMapper();
    @Spy
    private CreateUserMapper createUserMapper = new CreateUserMapper();

    @InjectMocks
    private UserService userService;

    private static UserModel getUserModel(User user) {
        return UserModel.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }

    private static User getUser() {
        return User.builder()
                .userId(1L)
                .fullName("Test user")
                .email("test@example.com")
                .password("hashedPassword")
                .build();
    }

    @Test
    public void testGetAll() {
        User user = getUser();
        UserModel userModel = getUserModel(user);

        when(userRepository.getAll()).thenReturn(List.of(user));

        List<UserModel> actualModels = userService.getAll();

        assertEquals(1, actualModels.size());
        assertUsersEquals(userModel, actualModels.get(0));
    }

    @Test
    public void testGetById() {
        User user = getUser();
        UserModel userModel = getUserModel(user);
        long userId = user.getUserId();

        when(userRepository.getById(userId)).thenReturn(Optional.of(user));

        Optional<UserModel> actual = userService.getById(userId);

        assertTrue(actual.isPresent());
        assertUsersEquals(userModel, actual.get());
    }

    @Test
    public void testGetByEmail() {
        User user = getUser();
        UserModel userModel = getUserModel(user);
        String email = userModel.getEmail();

        when(userRepository.getByEmail(email)).thenReturn(Optional.of(user));

        Optional<UserModel> actual = userService.getByEmail(email);

        assertTrue(actual.isPresent());
        assertUsersEquals(userModel, actual.get());
    }

    @Test
    public void testCheckLoginWithCorrectCredentials() {
        String email = "test@example.com";
        String password = "password";
        String hashedPassword = PasswordHash.hashPassword(password);

        User user = getUser();
        user.setPassword(hashedPassword);

        when(userRepository.getByEmail(email)).thenReturn(Optional.of(user));

        assertTrue(userService.checkLogin(email, password).isEmpty());
    }

    @Test
    public void testCheckLoginWithIncorrectEmail() {
        String email = "nonexistent@example.com";
        String password = "password";

        when(userRepository.getByEmail(email)).thenReturn(Optional.empty());

        assertTrue(userService.checkLogin(email, password).isPresent());
    }

    @Test
    public void testCheckLoginWithIncorrectPassword() {
        String email = "test@example.com";
        String password = "incorrectPassword";
        String hashedPassword = PasswordHash.hashPassword("password");

        User user = getUser();
        user.setPassword(hashedPassword);

        when(userRepository.getByEmail(email)).thenReturn(Optional.of(user));

        assertTrue(userService.checkLogin(email, password).isPresent());
    }

    @Test
    public void testCreate() {
        User user = getUser();
        UserModel userModel = getUserModel(user);
        CreateUserModel createModel = CreateUserModel.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        when(userRepository.create(any())).thenReturn(user);

        assertUsersEquals(userModel, userService.create(createModel));
    }

    @Test
    public void testUpdate() {
        CreateUserModel createModel = CreateUserModel.builder()
                .email("test@example.com")
                .password("password")
                .build();
        User user = getUser();

        when(userRepository.getById(user.getUserId())).thenReturn(Optional.of(user));
        when(userRepository.update(user)).thenReturn(true);

        assertTrue(userService.update(1L, createModel));
    }

    @Test
    public void testDelete() {
        long userId = 1L;
        when(userRepository.delete(userId)).thenReturn(true);

        assertTrue(userService.delete(userId));
    }

    private void assertUsersEquals(UserModel expected, UserModel actual) {
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getFullName(), actual.getFullName());
    }
}
