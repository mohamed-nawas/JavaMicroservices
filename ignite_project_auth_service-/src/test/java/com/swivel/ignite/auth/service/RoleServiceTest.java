package com.swivel.ignite.auth.service;

import com.swivel.ignite.auth.entity.Role;
import com.swivel.ignite.auth.exception.AuthException;
import com.swivel.ignite.auth.exception.UserRoleNotFoundException;
import com.swivel.ignite.auth.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class tests {@link RoleService} class
 */
class RoleServiceTest {

    private static final String ROLE_ID = "rid-123456789";
    private static final String ROLE_NAME_ADMIN = "ADMIN";
    private static final String ROLE_NAME_USER = "USER";
    private static final String ERROR = "ERROR";
    @Mock
    private RoleRepository roleRepository;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        roleService = new RoleService(roleRepository);
    }

    /**
     * Start of tests for getRoleByName method
     */
    @Test
    void Should_ReturnRole_When_GettingRoleByNameIsSuccessful() {
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(getSampleRole()));

        assertEquals(ROLE_ID, roleService.getRoleByName(ROLE_NAME_ADMIN).getId());
    }

    @Test
    void Should_ThrowUserRoleNotFoundException_When_GettingRoleByNameForUserRoleNotFoundInDB() {
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

        UserRoleNotFoundException exception = assertThrows(UserRoleNotFoundException.class, () ->
                roleService.getRoleByName(ROLE_NAME_ADMIN));

        assertEquals("User role is not found in DB", exception.getMessage());
    }

    @Test
    void Should_ThrowOpenFashionAuthException_When_GettingRoleByNameIsFailed() {
        when(roleRepository.findByName(anyString())).thenThrow(new DataAccessException(ERROR) {
        });

        AuthException exception = assertThrows(AuthException.class, () ->
                roleService.getRoleByName(ROLE_NAME_ADMIN));

        assertEquals("Failed to get role from DB by name for name: " + ROLE_NAME_ADMIN, exception.getMessage());
    }


    /**
     * This method returns a sample role
     *
     * @return Role
     */
    private Role getSampleRole() {
        Role role = new Role();
        role.setId(ROLE_ID);
        role.setName(ROLE_NAME_USER);
        return role;
    }
}
