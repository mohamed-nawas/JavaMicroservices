package com.swivel.ignite.auth.controllers;

import com.swivel.ignite.auth.dto.request.UserRegistrationRequestDto;
import com.swivel.ignite.auth.entity.Role;
import com.swivel.ignite.auth.entity.User;
import com.swivel.ignite.auth.enums.ErrorResponseStatusType;
import com.swivel.ignite.auth.enums.RoleType;
import com.swivel.ignite.auth.enums.SuccessResponseStatusType;
import com.swivel.ignite.auth.exception.*;
import com.swivel.ignite.auth.service.RoleService;
import com.swivel.ignite.auth.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This class tests {@link UserController} class
 */
class UserControllerTest {

    private static final String USER_NAME = "lionelmessi";
    private static final String PASSWORD = "123456789";
    private static final RoleType ROLE_TYPE_STUDENT = RoleType.STUDENT;
    private static final String SUCCESS_STATUS = "SUCCESS";
    private static final String ERROR_STATUS = "ERROR";
    private static final String SUCCESS_MESSAGE = "Successfully returned the data.";
    private static final String ERROR_MESSAGE = "Oops!! Something went wrong. Please try again.";
    private static final String FAILED = "failed";
    private static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";

    private static final String CREATE_USER_URI = "/api/v1/auth/users/register";
    private static final String DELETE_USER_URI = "/api/v1/auth/users/delete/{username}";

    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        initMocks(this);
        UserController userController = new UserController(userService, roleService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new CustomizedExceptionHandling())
                .build();
    }

    /**
     * Start of tests for createUser method
     * Api context: /api/v1/auth/users/register
     */
    @Test
    void Should_ReturnOk_When_CreatingUserIsSuccessful() throws Exception {
        when(roleService.getRoleByName(anyString())).thenReturn(new Role());
        doNothing().when(userService).createUser(any(User.class));

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URI)
                        .content(getSampleUserRegistrationRequestDto().toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status").value(SUCCESS_STATUS))
                .andExpect(jsonPath("$.message").value(SuccessResponseStatusType.CREATE_USER.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(SuccessResponseStatusType.CREATE_USER.getCode()))
                .andExpect(jsonPath("$.data.userName").value(USER_NAME))
                .andExpect(jsonPath("$.displayMessage").value(SUCCESS_MESSAGE));
    }

    @Test
    void Should_ReturnBadRequest_When_CreatingUserForMissingRequiredFields() throws Exception {
        UserRegistrationRequestDto dto = getSampleUserRegistrationRequestDto();
        dto.setRoleType(null);

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URI)
                        .content(dto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
                        .getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
                        .getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    @Test
    void Should_ReturnBadRequest_When_CreatingUserForRoleNotFound() throws Exception {
        when(roleService.getRoleByName(anyString())).thenThrow(new UserRoleNotFoundException(FAILED));

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URI)
                        .content(getSampleUserRegistrationRequestDto().toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.ROLE_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.ROLE_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    @Test
    void Should_ReturnBadRequest_When_CreatingUserForUserAlreadyExists() throws Exception {
        when(roleService.getRoleByName(anyString())).thenReturn(new Role());
        doThrow(new UserAlreadyExistsException(FAILED)).when(userService).createUser(any(User.class));

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URI)
                        .content(getSampleUserRegistrationRequestDto().toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.USER_ALREADY_EXISTS.getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.USER_ALREADY_EXISTS.getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    @Test
    void Should_ReturnInternalServerError_When_CreatingUserIsFailed() throws Exception {
        when(roleService.getRoleByName(anyString())).thenThrow(new AuthException(FAILED));

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URI)
                        .content(getSampleUserRegistrationRequestDto().toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                        .getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                        .getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    /**
     * Start of tests for deleteUser method
     * Api context: /api/v1/auth/users/delete/{username}
     */
    @Test
    void Should_ReturnOk_When_DeletingUserIsSuccessful() throws Exception {
        doNothing().when(userService).deleteUser(anyString());

        String uri = DELETE_USER_URI.replace("{username}", USER_NAME);
        mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status").value(SUCCESS_STATUS))
                .andExpect(jsonPath("$.message").value(SuccessResponseStatusType.DELETE_USER.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(SuccessResponseStatusType.DELETE_USER.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.displayMessage").value(SUCCESS_MESSAGE));
    }

    @Test
    void Should_ReturnBadRequest_When_DeletingUserForUserNotFound() throws Exception {
        doThrow(new UserNotFoundException(FAILED)).when(userService).deleteUser(anyString());

        String uri = DELETE_USER_URI.replace("{username}", USER_NAME);
        mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.USER_NOT_FOUND
                        .getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.USER_NOT_FOUND
                        .getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    @Test
    void Should_ReturnInternalServerError_When_DeletingUserIsFailed() throws Exception {
        doThrow(new AuthException(FAILED)).when(userService).deleteUser(anyString());

        String uri = DELETE_USER_URI.replace("{username}", USER_NAME);
        mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                        .getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                        .getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    /**
     * This method returns a sample UserRegistrationRequestDto
     *
     * @return UserRegistrationRequestDto
     */
    private UserRegistrationRequestDto getSampleUserRegistrationRequestDto() {
        UserRegistrationRequestDto dto = new UserRegistrationRequestDto();
        dto.setUsername(USER_NAME);
        dto.setPassword(PASSWORD);
        dto.setRoleType(ROLE_TYPE_STUDENT);
        return dto;
    }
}
