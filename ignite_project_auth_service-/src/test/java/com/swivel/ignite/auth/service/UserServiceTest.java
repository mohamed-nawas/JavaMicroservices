package com.swivel.ignite.auth.service;

import com.swivel.ignite.auth.entity.User;
import com.swivel.ignite.auth.exception.AuthException;
import com.swivel.ignite.auth.exception.UserAlreadyExistsException;
import com.swivel.ignite.auth.exception.UserNotFoundException;
import com.swivel.ignite.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class tests {@link UserService} class
 */
class UserServiceTest {

    private static final String USER_ID = "uid-123456789";
    private static final String USER_NAME = "Mohamed Nawaz";
    private static final String USER_PASSWORD = "123456789";
    private static final String ERROR = "ERROR";
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        userService = new UserService(userRepository, passwordEncoder);
    }

    /**
     * Start of tests for createUser method
     */
    @Test
    void Should_CreateUserInDBWithEncodedPwd_When_CreatingUserIsSuccessful() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        userService.createUser(getSampleUser());

        verify(passwordEncoder).encode(anyString());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void Should_ThrowOpenFashionAuthException_When_UserExistenceCheckFailedForCreateUser() {
        User user = getSampleUser();

        when(userRepository.findById(USER_ID)).thenThrow(new DataAccessException(ERROR) {
        });
        AuthException exception = assertThrows(AuthException.class, () ->
                userService.createUser(user));
        assertEquals("Failed to check for user existence in DB for id: " + USER_ID, exception.getMessage());
    }

    @Test
    void Should_ThrowUserAlreadyExistsException_When_UserAlreadyExistsInDBForCreateUser() {
        User user = getSampleUser();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () ->
                userService.createUser(user));
        assertEquals("User already exists in DB", exception.getMessage());
    }

    @Test
    void Should_ThrowOpenFashionAuthException_When_CreatingUserIsFailed() {
        User user = getSampleUser();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenThrow(new DataAccessException(ERROR) {
        });
        AuthException exception = assertThrows(AuthException.class, () ->
                userService.createUser(user));
        assertEquals("Failed to save user to DB for user id: {}" + USER_ID, exception.getMessage());
    }

    /**
     * Start of tests for deleteUser method
     */
    @Test
    void Should_DeleteUser_When_DeletingUserIsSuccessful() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(getSampleUser()));
        userService.deleteUser(USER_NAME);
        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    void Should_ThrowUserNotFoundException_When_DeletingUserForUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService
                .deleteUser(USER_NAME));
        assertEquals("User is not found in auth db for username: " + USER_NAME, exception.getMessage());
    }

    @Test
    void Should_ThrowAuthException_When_DeletingUserIsFailed() {
        when(userRepository.findByUsername(anyString())).thenThrow(new DataAccessException(ERROR) {
        });
        AuthException exception = assertThrows(AuthException.class, () -> userService
                .deleteUser(USER_NAME));
        assertEquals("Failed to delete user from auth db for username: " + USER_NAME, exception.getMessage());
    }


    /**
     * This method returns a sample user
     *
     * @return User
     */
    private User getSampleUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setPassword(USER_PASSWORD);
        return user;
    }
}
