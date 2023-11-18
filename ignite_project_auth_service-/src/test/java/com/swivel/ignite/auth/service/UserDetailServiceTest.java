package com.swivel.ignite.auth.service;

import com.swivel.ignite.auth.entity.Role;
import com.swivel.ignite.auth.entity.User;
import com.swivel.ignite.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class tests {@link UserDetailService} class
 */
class UserDetailServiceTest {

    private static final String USER_ID = "uid-123456789";
    private static final String USER_ROLE = "USER";
    private static final String USER_PASSWORD = "123456789";
    private static final String ERROR = "ERROR";
    @Mock
    private UserRepository userRepository;
    private UserDetailService userDetailService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        userDetailService = new UserDetailService(userRepository);
    }

    /**
     * Start of tests for loadUserByUsername method
     */
    @Test
    void Should_ReturnAuthUserDetail_When_LoadingUserByUsernameForUsernameIsSuccessful() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(getSampleUser()));

        assertEquals(USER_ID, userDetailService.loadUserByUsername(USER_ID).getUsername());
        assertEquals(USER_PASSWORD, userDetailService.loadUserByUsername(USER_ID).getPassword());
        assertEquals(1, userDetailService.loadUserByUsername(USER_ID).getAuthorities().size());
    }

    @Test
    void Should_ReturnAuthUserDetail_When_LoadingUserByUsernameForIdIsSuccessful() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findById(anyString())).thenReturn(Optional.of(getSampleUser()));

        assertEquals(USER_ID, userDetailService.loadUserByUsername(USER_ID).getUsername());
        assertEquals(USER_PASSWORD, userDetailService.loadUserByUsername(USER_ID).getPassword());
        assertEquals(1, userDetailService.loadUserByUsername(USER_ID).getAuthorities().size());
    }

    @Test
    void Should_ThrowUsernameNotFoundException_When_LoadingUserByUsernameForInvalidUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                userDetailService.loadUserByUsername(USER_ID));
        assertEquals("Invalid username", exception.getMessage());
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
        user.setRole(getSampleRole());
        return user;
    }

    /**
     * This method returns a sample role
     *
     * @return Role
     */
    private Role getSampleRole() {
        Role role = new Role();
        role.setName(USER_ROLE);
        return role;
    }
}
