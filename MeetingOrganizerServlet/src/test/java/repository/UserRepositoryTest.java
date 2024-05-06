package repository;

import entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repositories.UserRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRepositoryTest {
    private final TestConnectionManager connectionManager = new TestConnectionManager();
    private final UserRepository userRepository = new UserRepository(connectionManager);

    private User createSampleUser() {
        return User.builder()
                .fullName("Test User")
                .email("test@example.com")
                .password("password")
                .build();
    }

    @BeforeEach
    @AfterEach
    public void clear() {
        try (Connection connection = connectionManager.getConnection()) {
            String CLEAR_TABLE = "TRUNCATE users RESTART IDENTITY CASCADE";
            connection.prepareStatement(CLEAR_TABLE).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Create User - Success")
    void testCreateUser_Success() {
        User user = createSampleUser();
        User createdUser = userRepository.create(user);

        assertEquals(user.getFullName(), createdUser.getFullName());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getPassword(), createdUser.getPassword());
    }

    @Test
    @DisplayName("Get User By Id - Success")
    void testGetUserById_Success() {
        User user = createSampleUser();
        User createdUser = userRepository.create(user);

        Optional<User> retrievedUserOpt = userRepository.getById(createdUser.getUserId());
        assertTrue(retrievedUserOpt.isPresent());

        User retrievedUser = retrievedUserOpt.get();
        assertEquals(createdUser.getUserId(), retrievedUser.getUserId());
        assertEquals(createdUser.getFullName(), retrievedUser.getFullName());
        assertEquals(createdUser.getEmail(), retrievedUser.getEmail());
        assertEquals(createdUser.getPassword(), retrievedUser.getPassword());
    }

    @Test
    @DisplayName("Get User By Id - Not Found")
    void testGetUserById_NotFound() {
        Optional<User> retrievedUserOpt = userRepository.getById(-1L);
        assertFalse(retrievedUserOpt.isPresent());
    }

    @Test
    @DisplayName("Update User - Success")
    void testUpdateUser_Success() {
        User user = createSampleUser();
        User createdUser = userRepository.create(user);

        createdUser.setFullName("Test User");
        createdUser.setEmail("test@example.com");
        createdUser.setPassword("newPassword");

        assertTrue(userRepository.update(createdUser));
        Optional<User> updatedUserOpt = userRepository.getById(createdUser.getUserId());
        assertTrue(updatedUserOpt.isPresent());

        User updatedUser = updatedUserOpt.get();
        assertEquals(createdUser.getUserId(), updatedUser.getUserId());
        assertEquals(createdUser.getFullName(), updatedUser.getFullName());
        assertEquals(createdUser.getEmail(), updatedUser.getEmail());
        assertEquals(createdUser.getPassword(), updatedUser.getPassword());
    }

    @Test
    @DisplayName("Delete User - Success")
    void testDeleteUser_Success() {
        User user = createSampleUser();
        User createdUser = userRepository.create(user);

        assertTrue(userRepository.delete(createdUser.getUserId()));
        Optional<User> deletedUserOpt = userRepository.getById(createdUser.getUserId());
        assertFalse(deletedUserOpt.isPresent());
    }

    @Test
    @DisplayName("Get All Users - Success")
    void testGetAllUsers_Success() {
        User user1 = createSampleUser();
        User user2 = createSampleUser();
        user2.setEmail("jane.doe@example.com");

        userRepository.create(user1);
        userRepository.create(user2);

        List<User> users = userRepository.getAll();
        assertEquals(2, users.size());
    }

    @Test
    @DisplayName("Get User By Email - Success")
    void testGetUserByEmail_Success() {
        User user = createSampleUser();
        userRepository.create(user);

        Optional<User> retrievedUserOpt = userRepository.getByEmail(user.getEmail());
        assertTrue(retrievedUserOpt.isPresent());

        User retrievedUser = retrievedUserOpt.get();
        assertEquals(user.getUserId(), retrievedUser.getUserId());
        assertEquals(user.getFullName(), retrievedUser.getFullName());
        assertEquals(user.getEmail(), retrievedUser.getEmail());
        assertEquals(user.getPassword(), retrievedUser.getPassword());
    }

    @Test
    @DisplayName("Get User By Email - Not Found")
    void testGetUserByEmail_NotFound() {
        Optional<User> retrievedUserOpt = userRepository.getByEmail("nonexistent@example.com");
        assertFalse(retrievedUserOpt.isPresent());
    }

    @Test
    @DisplayName("Get User By Email And Password - Success")
    void testGetUserByEmailAndPassword_Success() {
        User user = createSampleUser();
        userRepository.create(user);

        Optional<User> retrievedUserOpt = userRepository.getByEmailAndPassword(user.getEmail(), user.getPassword());
        assertTrue(retrievedUserOpt.isPresent());

        User retrievedUser = retrievedUserOpt.get();
        assertEquals(user.getUserId(), retrievedUser.getUserId());
        assertEquals(user.getFullName(), retrievedUser.getFullName());
        assertEquals(user.getEmail(), retrievedUser.getEmail());
        assertEquals(user.getPassword(), retrievedUser.getPassword());
    }

    @Test
    @DisplayName("Get User By Email And Password - Not Found")
    void testGetUserByEmailAndPassword_NotFound() {
        Optional<User> retrievedUserOpt = userRepository.getByEmailAndPassword("nonexistent@example.com", "password");
        assertFalse(retrievedUserOpt.isPresent());
    }

    @Test
    @DisplayName("Update User - Not Found")
    void testUpdateUser_NotFound() {
        User user = createSampleUser();
        user.setUserId(-1L);

        assertFalse(userRepository.update(user));
    }

    @Test
    @DisplayName("Delete User - Not Found")
    void testDeleteUser_NotFound() {
        assertFalse(userRepository.delete(-1L));
    }
}
